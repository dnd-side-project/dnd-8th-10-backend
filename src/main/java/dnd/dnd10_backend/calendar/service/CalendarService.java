package dnd.dnd10_backend.calendar.service;

import dnd.dnd10_backend.calendar.domain.TimeCard;
import dnd.dnd10_backend.calendar.domain.UserTimeCard;
import dnd.dnd10_backend.calendar.dto.request.TimeCardCreateDto;
import dnd.dnd10_backend.calendar.dto.request.UpdateTimeCardRequestDto;
import dnd.dnd10_backend.calendar.dto.response.TimeCardResponseDto;
import dnd.dnd10_backend.calendar.repository.TimeCardRepository;
import dnd.dnd10_backend.calendar.repository.UserTimeCardRepository;
import dnd.dnd10_backend.checkList.domain.DefaultCheckList;
import dnd.dnd10_backend.checkList.service.CheckListService;
import dnd.dnd10_backend.checkList.service.DefaultCheckListService;
import dnd.dnd10_backend.common.domain.enums.CodeStatus;
import dnd.dnd10_backend.common.exception.CustomerNotFoundException;
import dnd.dnd10_backend.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 패키지명 dnd.dnd10_backend.calendar.service
 * 클래스명 CalendarService
 * 클래스설명
 * 작성일 2023-02-06
 *
 * @author 이우진
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 * [2023-02-06] TimeCard 저장 기능 구현 - 이우진
 * [2023-02-08] TimeCard 수정, 삭제, 조회 기능 구현 - 이우진
 * [2023-02-08] GET, DELETE 요청 파라미터 수정 - 이우진
 * [2023-02-11] getTimeCards 유저 프로필 코드 추가 - 이우진
 * [2023-02-11] workPlace storeName 으로 수정 - 이우진
 * [2023-02-16] 출근 시 대타일자면 체크리스트 생성하도록 변경 - 원지윤
 * [2023-02-18] 날짜 포맷으로 발생하는 에러 해결 - 원지윤
 * [2023-02-20] timeCardRepository user 사용 메서드 userCode로 변경 - 이우진
 */
@Service
@RequiredArgsConstructor
public class CalendarService {

    private final TimeCardRepository timeCardRepository;
    private final CheckListService checkListService;
    private final DefaultCheckListService defaultCheckListService;
    private final UserTimeCardRepository userTimeCardRepository;
    
    @Transactional
    public void saveTimeCard(TimeCardCreateDto request, User user) {
        //입력 된 날짜를 LocalDate타입으로 변경
        LocalDate now = convertDate(request);

        //입력 된 날짜가 원래 일하는 날인지 확인
        if(!checkListService.checkWorkDay(now,user)){
            //기본 체크리스트 생성
            defaultCheckListService.saveDefaultCheckList(now, user);
        }

        TimeCard timeCard = timeCardRepository.save(request.toEntity(user));
        userTimeCardRepository.save(UserTimeCard.builder()
                .userName(user.getUsername())
                .userCode(user.getUserCode())
                .userProfileCode(user.getUserProfileCode())
                .timeCardId(timeCard.getId())
                .workTime(user.getWorkTime())
                .build()
        );
    }

    @Transactional
    public void updateTimeCard(UpdateTimeCardRequestDto requestDto, User user) {
        TimeCard timeCard = timeCardRepository.findByYearAndMonthAndDayAndUserCode(requestDto.getYear(),
                requestDto.getMonth(),
                requestDto.getDay(),
                        user.getUserCode())
                .orElseThrow(() -> new CustomerNotFoundException(CodeStatus.NOT_FOUND_TIMECARD));

        timeCard.update(requestDto.getWorkTime(), requestDto.getWorkHour());
        timeCardRepository.save(timeCard);
    }

    @Transactional
    public void deleteTimeCard(String year, String month, String day, User user) {

        TimeCard timeCard = timeCardRepository.findByYearAndMonthAndDayAndUserCode(year, month, day, user.getUserCode())
                .orElseThrow(() -> new CustomerNotFoundException(CodeStatus.NOT_FOUND_TIMECARD));

        UserTimeCard userTimeCard = userTimeCardRepository.findByTimeCardId(timeCard.getId())
                .orElseThrow(() -> new CustomerNotFoundException(CodeStatus.NOT_FOUND_TIMECARD));
        timeCardRepository.delete(timeCard);
        userTimeCardRepository.delete(userTimeCard);
    }


    public List<TimeCardResponseDto> getTimeCards(String year, String month, String day, String storeName) {
        List<TimeCard> timeCards =
                timeCardRepository.findByYearAndMonthAndDayAndStoreName(year, month, day, storeName);

        // 이 부분 이쁘게 리팩토링 하고 싶다.
        List<UserTimeCard> userTimeCards = null;
        for(TimeCard timeCard : timeCards) {
            userTimeCards.add(userTimeCardRepository.findByTimeCardId(timeCard.getId()).orElseThrow(IllegalArgumentException::new));
        }

        List<TimeCardResponseDto> collect = userTimeCards.stream()
                .map(u -> new TimeCardResponseDto(u.getUserName(), u.getWorkTime(), u.getUserProfileCode()))
                .collect(Collectors.toList());

        return collect;
    }

    public List<String> getWorkDay(String year, String month, User user) {
        List<TimeCard> timeCards = timeCardRepository.findByYearAndMonthAndUserCode(year, month, user.getUserCode());
        List<String> collect = timeCards.stream()
                .map(t -> new String(t.getDay()))
                .collect(Collectors.toList());

        return collect;
    }

    /**
     * request에 들어온 날을 LocalDate으로 변경해주는 메소드
     * @param request
     * @return
     */
    public LocalDate convertDate(TimeCardCreateDto request){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String month = request.getMonth().length()<2 ? "0"+request.getMonth() : request.getMonth();
        String day = request.getDay().length()<2 ? "0"+request.getDay() : request.getDay();
        String nowStr = request.getYear()+"-"+month+"-"+day;
        return LocalDate.parse(nowStr, format);
    }
}
