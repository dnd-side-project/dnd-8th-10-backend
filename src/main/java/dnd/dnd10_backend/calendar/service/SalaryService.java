package dnd.dnd10_backend.calendar.service;

import dnd.dnd10_backend.calendar.domain.TimeCard;
import dnd.dnd10_backend.calendar.dto.response.SalaryDetailResponseDto;
import dnd.dnd10_backend.calendar.dto.response.SalaryResponseDto;
import dnd.dnd10_backend.calendar.dto.response.StoreSalaryResponseDto;
import dnd.dnd10_backend.calendar.repository.TimeCardRepository;
import dnd.dnd10_backend.store.domain.Store;
import dnd.dnd10_backend.user.domain.User;
import dnd.dnd10_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 패키지명 dnd.dnd10_backend.calendar.service
 * 클래스명 SalaryService
 * 클래스설명
 * 작성일 2023-02-12
 *
 * @author 이우진
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 * [2023-02-12] 직원페이지 급여 조회 기능 개발 - 이우진
 * [2023-02-13] 점장 급여 조회 기능, 상세 조회 기능 개발 - 이우진
 * [2023-02-20] timeCardRepository user 사용 메서드 userCode로 변경 - 이우진
 * [2023-02-25] 시급 값 변경 - 이우진
 */

@Service
@RequiredArgsConstructor
public class SalaryService {

    private final TimeCardRepository timeCardRepository;
    private final UserRepository userRepository;

    public List<SalaryResponseDto> getWorkerSalary(String year, String month, User user) {
        List<TimeCard> timeCards = timeCardRepository.findByYearAndMonthAndUserCode(year, month, user.getUserCode());
        List<SalaryResponseDto> collect = timeCards.stream()
                .map(t -> new SalaryResponseDto(t.getMonth(), t.getDay(), t.getWorkTime(), t.getWorkHour(), t.getWorkHour()*user.getWage()))
                .collect(Collectors.toList());

        return collect;
    }

    public List<StoreSalaryResponseDto> getStoreSalary(String year, String month, Store store) {
        List<User> member = userRepository.findByStore(store);
        List<StoreSalaryResponseDto> dtos = new ArrayList<>();

        for (User user : member) {
            List<TimeCard> timeCards = timeCardRepository.findByYearAndMonthAndUserCode(year, month, user.getUserCode());
            Double sum = timeCards.stream().mapToDouble(TimeCard::getWorkHour).sum();
            Double salary = sum * user.getWage();
            StoreSalaryResponseDto responseDto = StoreSalaryResponseDto.builder()
                    .userCode(user.getUserCode())
                    .userName(user.getUsername())
                    .role(user.getRole())
                    .userProfileCode(user.getUserProfileCode())
                    .totalSalary(salary)
                    .build();

            dtos.add(responseDto);
        }
        return dtos;
    }

    public SalaryDetailResponseDto getSalaryDetail(String year, String month, User user) {
        List<TimeCard> timeCards = timeCardRepository.findByYearAndMonthAndUserCode(year, month, user.getUserCode());
        List<SalaryResponseDto> collect = timeCards.stream()
                .map(t -> new SalaryResponseDto(t.getMonth(), t.getDay(), t.getWorkTime(), t.getWorkHour(), t.getWorkHour()*user.getWage()))
                .collect(Collectors.toList());

        Double totalSalary = collect.stream().mapToDouble(SalaryResponseDto::getWorkHour).sum();

        return SalaryDetailResponseDto.builder()
                .userProfileCode(user.getUserProfileCode())
                .userName(user.getUsername())
                .role(user.getRole())
                .workTime(user.getWorkTime())
                .totalSalary(totalSalary)
                .daySalary(collect)
                .build();
    }
}
