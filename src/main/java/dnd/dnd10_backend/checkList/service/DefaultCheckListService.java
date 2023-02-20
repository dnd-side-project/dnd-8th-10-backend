package dnd.dnd10_backend.checkList.service;

import dnd.dnd10_backend.checkList.domain.CheckList;
import dnd.dnd10_backend.checkList.domain.DefaultCheckList;
import dnd.dnd10_backend.checkList.repository.CheckListRepository;
import dnd.dnd10_backend.checkList.repository.DefaultCheckListRepository;
import dnd.dnd10_backend.user.domain.User;
import dnd.dnd10_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
/**
 * 패키지명 dnd.dnd10_backend.checkList.service
 * 클래스명 DefaultCheckListService
 * 클래스설명 기본 체크리스트 관련 service clss
 * 작성일 2023-02-15
 * 
 * @author 원지윤
 * @version 1.0
 * [수정내용] 
 * 예시) [2022-09-17] 주석추가 - 원지윤
 * [2023-02-16] 지난주 체크리스트를 삭제하는 스케쥴러 추가 - 원지윤
 */
@Service
@RequiredArgsConstructor
public class DefaultCheckListService {

    private final UserRepository userRepository;
    private final DefaultCheckListRepository defaultCheckListRepository;
    private final CheckListRepository checkListRepository;

    /**
     * 매주 일요일 0시 0분 0초에 사용자들이 일하는 시간의 기본 체크리스트를 생성해주는 메소드
     */
    @Scheduled(cron = "0 0 0 * * SUN") //매주 일요일 오전 12시마다 실행
    public void saveDefaultCheckListByScheduled() {
        List<User> userList = userRepository.findAll();
        LocalDate now = LocalDate.now(ZoneId.of("Asia/Seoul")); // 현재시간
        for(User user: userList) {
           String[] workDayList = user.getWorkTime().split(",");
           for(String workDay: workDayList){
               String day = workDay.substring(0,1);
               int saveDay = 0;
               switch (day){
                   case "월" : saveDay = 1; break;
                   case "화" : saveDay = 2; break;
                   case "수" : saveDay = 3; break;
                   case "목" : saveDay = 4; break;
                   case "금" : saveDay = 5; break;
                   case "토" : saveDay = 6; break;
                   case "일": break;
               }
               saveDefaultCheckList(now.plusDays(saveDay), user);
           }
        }
        return;
    }

    /**
     * 일주일 지난 기록들을 삭제하는 메소드
     */
    @Scheduled(cron = "0 0 3 * * SUN")
    public void deleteCheckListByScheduled(){
        LocalDate date = LocalDate.now(ZoneId.of("Asia/Seoul")).minusDays(2);
        List<CheckList> checkList = checkListRepository.findCheckListByPastDate(date);

        for(CheckList cl: checkList){
            checkListRepository.delete(cl);
        }
        return;
    }

    /**
     * 기본 체크리스트를 생성해주는 메소드
     * @param date checkList를 등록할 날짜
     * @param user checkList를 등록한 사람
     * @return
     */
    public List<CheckList> saveDefaultCheckList(LocalDate date,User user){
        List<DefaultCheckList> defaultList = defaultCheckListRepository.findAll();
        List<CheckList> checkList = checkListRepository.findCheckListByDateAndUser(date, user);
        if(!checkList.isEmpty()) return checkList;

        for(DefaultCheckList check: defaultList) {
            checkList.add(
                    CheckList.builder()
                            .user(user)
                            .date(date)
                            .content(check.getContent())
                            .status("N")
                            .build()
            );
        }

        checkListRepository.saveAll(checkList);
        return checkList;
    }

}
