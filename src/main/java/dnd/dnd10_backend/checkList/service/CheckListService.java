package dnd.dnd10_backend.checkList.service;

import com.auth0.jwt.algorithms.Algorithm;
import dnd.dnd10_backend.checkList.domain.CheckList;
import dnd.dnd10_backend.checkList.dto.request.CheckListRequestDto;
import dnd.dnd10_backend.checkList.dto.request.DeleteCheckListRequestDto;
import dnd.dnd10_backend.checkList.dto.request.SearchCheckListRequestDto;
import dnd.dnd10_backend.checkList.dto.request.UpdateCheckListRequestDto;
import dnd.dnd10_backend.checkList.dto.response.CheckListResponseDto;
import dnd.dnd10_backend.checkList.dto.response.WorkCheckListResponseDto;
import dnd.dnd10_backend.checkList.repository.CheckListRepository;
import dnd.dnd10_backend.common.domain.enums.CodeStatus;
import dnd.dnd10_backend.common.exception.CustomerNotFoundException;
import dnd.dnd10_backend.config.jwt.JwtProperties;
import dnd.dnd10_backend.user.domain.User;
import dnd.dnd10_backend.user.repository.UserRepository;
import org.hibernate.annotations.Check;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.auth0.jwt.JWT.require;

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
 */
@Service
public class CheckListService {
    @Autowired
    private CheckListRepository checkListRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * 체크리스트 저장하는 메소드
     * @param token
     * @param requestDto
     * @return
     */
    public List<CheckListResponseDto> saveCheckList(CheckListRequestDto requestDto, final String token){
        String email = require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token)
                .getClaim("email").asString();

        //user 찾기
        User user = userRepository.findByKakaoEmail(email);
        if(user == null) throw new CustomerNotFoundException(CodeStatus.NOT_FOUND_USER);

        CheckList checkList = CheckList.builder()
                .content(requestDto.getContent())
                .date(requestDto.getDate())
                .status(requestDto.getStatus())
                .user(user)
                .build();

        checkListRepository.save(checkList);
        
        return findCheckListByDate(requestDto.getDate(), user);
    }

    /**
     *  체크리스트 조회하는 메소드
     * @param requestDto
     * @param token
     * @return
     */
    public WorkCheckListResponseDto findCheckList(SearchCheckListRequestDto requestDto, final String token){
        String email = require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token)
                .getClaim("email").asString();

        //user 찾기
        User user = userRepository.findByKakaoEmail(email);

        if(user == null) throw new CustomerNotFoundException(CodeStatus.NOT_FOUND_USER);

        //일하는 날인지 체크
        boolean isWorkDay = checkWorkDay(requestDto.getDate(), user);

        return WorkCheckListResponseDto.of(isWorkDay, findCheckListByDate(requestDto.getDate(), user));

    }

    /**
     * 체크리스트 삭제하는 메소드
     * @param requestDto
     * @param token
     * @return
     */
    public List<CheckListResponseDto> deleteCheckList(DeleteCheckListRequestDto requestDto, final String token){
        String email = require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token)
                .getClaim("email").asString();

        //user 찾기
        User user = userRepository.findByKakaoEmail(email);

        if(user == null) throw new CustomerNotFoundException(CodeStatus.NOT_FOUND_USER);

        CheckList checkList = checkListRepository.findById(requestDto.getCheckIdx())
                .orElseThrow(() -> new CustomerNotFoundException(CodeStatus.NOT_FOUND_CHECKLIST));

        checkListRepository.delete(checkList);

        return findCheckListByDate(checkList.getDate(), user);
    }

    /**
     * 체크리스트 업데이트하는 메소드
     * @param requestDto
     * @param token
     */
    public List<CheckListResponseDto> updateCheckList(UpdateCheckListRequestDto requestDto, final String token){
        String email = require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token)
                .getClaim("email").asString();

        //user 찾기
        User user = userRepository.findByKakaoEmail(email);

        if(user == null) throw new CustomerNotFoundException(CodeStatus.NOT_FOUND_USER);
        
        //기존 체크리스트 찾기
        CheckList checkList = checkListRepository.findById(requestDto.getCheckIdx())
                .orElseThrow(() -> new CustomerNotFoundException(CodeStatus.NOT_FOUND_CHECKLIST));
        
        //체크리스트 내용 업데이트
        checkList.update(requestDto);
        
        //업데이트 내용 저장
        checkListRepository.save(checkList);

        return findCheckListByDate(checkList.getDate(), user);
    }

    /**
     * 조회하려는 날짜가 일하는 날인지 확인하는 메소드
     * @param date 조회하려는 날짜
     * @param user 조회하려는 사용자
     * @return
     */
    public boolean checkWorkDay(LocalDate date, User user){
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        int dayOfWeekNumber = dayOfWeek.getValue();
        //월 = 1 ~ 일 = 7
        String DOW = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN);

        return user.getWorkTime().contains(DOW.subSequence(0,1));
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
                checkListRepository.findCheckListByDateAndUser(date, user);

        //response에 추가
        List<CheckListResponseDto> checkListResponseDtoList = new ArrayList<>();

        for(CheckList cl: checkLists){
            checkListResponseDtoList.add(CheckListResponseDto.of(cl));
        }

        return checkListResponseDtoList;
    }

}
