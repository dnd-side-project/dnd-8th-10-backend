package dnd.dnd10_backend.checkList.service;

import dnd.dnd10_backend.calendar.repository.TimeCardRepository;
import dnd.dnd10_backend.checkList.domain.CheckList;
import dnd.dnd10_backend.checkList.dto.request.CheckListRequestDto;
import dnd.dnd10_backend.checkList.dto.request.UpdateCheckListRequestDto;
import dnd.dnd10_backend.checkList.dto.response.CheckListResponseDto;
import dnd.dnd10_backend.checkList.dto.response.WorkCheckListResponseDto;
import dnd.dnd10_backend.checkList.repository.CheckListRepository;
import dnd.dnd10_backend.common.domain.enums.CodeStatus;
import dnd.dnd10_backend.common.exception.CustomerNotFoundException;
import dnd.dnd10_backend.user.domain.User;
import dnd.dnd10_backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * 패키지명 dnd.dnd10_backend.checkList.service
 * 클래스명 CheckListService
 * 클래스설명
 * 작성일 2023-02-05
 *
 * @author 원지윤
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 * [2023-02-08] 체크리스트 조회를 dto -> String으로 변경 - 원지윤
 * [2023-02-08] 체크리스트 삭제, 업데이트 시 사용자 확인 - 원지윤
 * [2023-02-13] 체크리스트 일주일 상태 확인 메소드 추가 - 원지윤
 * [2023-02-13] userService 추가 및 토큰으로 사용자 찾는 부분 변경 - 원지윤
 * [2023-02-16] 출근일인지 확인하는 부분 수정 - 원지윤
 */
@Service
@RequiredArgsConstructor
public class CheckListService {

    private final CheckListRepository checkListRepository;
    private final UserService userService;
    private final TimeCardRepository timeCardRepository;

    /**
     * 체크리스트 저장하는 메소드
     * @param token
     * @param requestDto
     * @return
     */
    @Transactional
    public List<CheckListResponseDto> saveCheckList(CheckListRequestDto requestDto, final String token){
        //user 찾기
        User user = userService.getUserByEmail(token);
        if(user == null) throw new CustomerNotFoundException(CodeStatus.NOT_FOUND_USER);

        CheckList checkList = CheckList.builder()
                .content(requestDto.getContent())
                .checkDate(requestDto.getDate())
                .status(requestDto.getStatus())
                .user(user)
                .build();

        checkListRepository.save(checkList);
        
        return findCheckListByDate(requestDto.getDate(), user);
    }

    /**
     * 체크리스트 조회하는 메소드
     * @param date 조회하려는 날짜
     * @param token 토큰
     * @return
     */
    public WorkCheckListResponseDto findCheckList(final String date, final String token){
        //user 찾기
        User user = userService.getUserByEmail(token);

        if(user == null) throw new CustomerNotFoundException(CodeStatus.NOT_FOUND_USER);

        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);

        boolean isWorkDay = false;
        //일하는 날인지 체크
        if(checkWorkDay(localDate, user) || checkInsteadWorkDay(localDate, user)){
            isWorkDay = true;
        }

        return WorkCheckListResponseDto.of(isWorkDay, findCheckListByDate(localDate, user));

    }

    /**
     * 체크리스트 삭제하는 메소드
     * @param checkIdx 삭제하려는 체크리스트 idx
     * @param token
     * @return
     */
    public List<CheckListResponseDto> deleteCheckList(Long checkIdx, final String token){
        //user 찾기
        User user = userService.getUserByEmail(token);

        if(user == null) throw new CustomerNotFoundException(CodeStatus.NOT_FOUND_USER);

        CheckList checkList = checkListRepository.findById(checkIdx)
                .orElseThrow(() -> new CustomerNotFoundException(CodeStatus.NOT_FOUND_CHECKLIST));

        if(!checkList.getUser().equals(user))
            throw new CustomerNotFoundException(CodeStatus.UNAUTHORIZED_DELETED_USER);

        checkListRepository.delete(checkList);

        return findCheckListByDate(checkList.getCheckDate(), user);
    }

    /**
     * 체크리스트 업데이트하는 메소드
     * @param requestDto
     * @param token
     */
    public List<CheckListResponseDto> updateCheckList(UpdateCheckListRequestDto requestDto, final String token){
        //user 찾기
        User user = userService.getUserByEmail(token);

        if(user == null) throw new CustomerNotFoundException(CodeStatus.NOT_FOUND_USER);
        
        //기존 체크리스트 찾기
        CheckList checkList = checkListRepository.findById(requestDto.getCheckIdx())
                .orElseThrow(() -> new CustomerNotFoundException(CodeStatus.NOT_FOUND_CHECKLIST));

        if(!checkList.getUser().equals(user)) {
            throw new CustomerNotFoundException(CodeStatus.UNAUTHORIZED_UPDATED_USER);
        }

        if(requestDto.getStatus().equals("Y") && checkList.getStatus().equals("N")) {
            checkList.setCheckedTime(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
        }

        //체크리스트 내용 업데이트
        checkList.update(requestDto);
        //업데이트 내용 저장
        checkListRepository.save(checkList);

        return findCheckListByDate(checkList.getCheckDate(), user);
    }

    /**
     * 조회하려는 날짜가 정규 출근 일인지 확인하는 메소드
     * @param date 조회하려는 날짜
     * @param user 조회하려는 사용자
     * @return
     */
    public boolean checkWorkDay(LocalDate date, User user){
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        String DOW = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN);

        return user.getWorkTime().contains(DOW.subSequence(0,1));
    }

    /**
     * 정규 출근일이 아닌 경우 출근부에 출근이 되어있는지 확인
     * @param date
     * @param user
     * @return
     */
    public boolean checkInsteadWorkDay(LocalDate date, User user){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String[] YMD = date.format(format).split("-");
        
        if(YMD[1].substring(0,1).equals("0")){
            YMD[1] = YMD[1].replace("0","");
        }

        if(YMD[2].substring(0,1).equals("0")){
            YMD[2] = YMD[2].replace("0","");
        }

        return timeCardRepository.findByYearAndMonthAndDayAndUserCode(YMD[0],YMD[1],YMD[2], user.getUserCode())
                .isPresent();
    }

    /**
     *
     * @param token access token
     * @return
     */
    public List<Boolean> checkWeek(final String token){
        List<Boolean> weekStatus = new ArrayList<>();
        User user = userService.getUserByEmail(token);

        LocalDate now = LocalDate.now(ZoneId.of("Asia/Seoul")); // 현재시간
        DayOfWeek dayOfWeek = now.getDayOfWeek();
        int dayOfWeekNumber = dayOfWeek.getValue(); //월 - 1 일 - 7

        LocalDate startDay = now;

        if(dayOfWeekNumber < 7)
            startDay = now.minusDays(dayOfWeekNumber);

        for(int i=0;i<7;i++){
            List<CheckList> list = checkListRepository
                    .findCheckListByCheckDateAndAndStatusAndUser(startDay.plusDays(i),"N", user);
            if(list.size()>0){
                weekStatus.add(true);
            }
            else{
                weekStatus.add(false);
            }
        }
        return weekStatus;
    }

    /**
     * 날짜로 checkListResponseDto list로 찾아주는 메소드
     * @param date 조회하려는 날짜
     * @param user 사용자
     * @return
     */
    public List<CheckListResponseDto> findCheckListByDate(LocalDate date, User user){
        //날짜로 checkList 찾기
        List<CheckList> checkLists =
                checkListRepository.findCheckListByCheckDateAndUser(date, user);

        //response에 추가
        List<CheckListResponseDto> checkListResponseDtoList = checkLists.stream()
                .map(t -> CheckListResponseDto.of(t))
                .collect(Collectors.toList());

        return checkListResponseDtoList;
    }

}
