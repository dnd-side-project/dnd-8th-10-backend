package dnd.dnd10_backend.calendar.service;

import dnd.dnd10_backend.calendar.domain.TimeCard;
import dnd.dnd10_backend.calendar.dto.request.CalendarRequestDto;
import dnd.dnd10_backend.calendar.dto.request.TimeCardCreateDto;
import dnd.dnd10_backend.calendar.dto.request.TimeCardRequestDto;
import dnd.dnd10_backend.calendar.dto.request.UpdateTimeCardRequestDto;
import dnd.dnd10_backend.calendar.dto.response.TimeCardResponseDto;
import dnd.dnd10_backend.calendar.repository.TimeCardRepository;
import dnd.dnd10_backend.common.domain.enums.CodeStatus;
import dnd.dnd10_backend.common.exception.CustomerNotFoundException;
import dnd.dnd10_backend.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
 * [2023-02-06] TimeCard 수정, 삭제, 조회 기능 구현 - 이우진
 */

@Service
@RequiredArgsConstructor
public class CalendarService {

    private final TimeCardRepository timeCardRepository;

    @Transactional
    public void saveTimeCard(TimeCardCreateDto request, User user) {
        timeCardRepository.save(request.toEntity(user));
    }

    @Transactional
    public void updateTimeCard(UpdateTimeCardRequestDto requestDto, User user) {
        TimeCard timeCard = timeCardRepository.findByYearAndMonthAndDayAndUser(requestDto.getYear(),
                requestDto.getMonth(),
                requestDto.getDay(),
                        user)
                .orElseThrow(() -> new CustomerNotFoundException(CodeStatus.NOT_FOUND_TIMECARD));

        timeCard.update(requestDto.getWorkTime(), requestDto.getWorkHour());
        timeCardRepository.save(timeCard);
    }

    @Transactional
    public void deleteTimeCard(TimeCardRequestDto requestDto, User user) {

        TimeCard timeCard = timeCardRepository.findByYearAndMonthAndDayAndUser(requestDto.getYear(),
                requestDto.getMonth(),
                requestDto.getDay(),
                user)
                .orElseThrow(() -> new CustomerNotFoundException(CodeStatus.NOT_FOUND_TIMECARD));

        timeCardRepository.delete(timeCard);
    }


    public List<TimeCardResponseDto> getTimeCards(TimeCardRequestDto requestDto, String workPlace) {
        List<TimeCard> timeCards =
                timeCardRepository.findByYearAndMonthAndDayAndWorkPlace(requestDto.getYear(),
                        requestDto.getMonth(),
                        requestDto.getDay(),
                        workPlace);
        List<TimeCardResponseDto> collect = timeCards.stream()
                .map(t -> new TimeCardResponseDto(t.getUser().getUsername(), t.getWorkTime()))
                .collect(Collectors.toList());

        return collect;
    }

    public List<String> getWorkDay(CalendarRequestDto requestDto, User user) {
        List<TimeCard> timeCards = timeCardRepository.findByYearAndMonthAndUser(requestDto.getYear(), requestDto.getMonth(), user);
        List<String> collect = timeCards.stream()
                .map(t -> new String(t.getDay()))
                .collect(Collectors.toList());

        return collect;
    }

}
