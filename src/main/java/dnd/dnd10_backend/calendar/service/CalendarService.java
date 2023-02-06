package dnd.dnd10_backend.calendar.service;

import dnd.dnd10_backend.calendar.dto.request.TimeCardCreateDto;
import dnd.dnd10_backend.calendar.dto.request.TimeCardRequestDto;
import dnd.dnd10_backend.calendar.repository.TimeCardRepository;
import dnd.dnd10_backend.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
 */

@Service
@RequiredArgsConstructor
public class CalendarService {

    private final TimeCardRepository timeCardRepository;

    @Transactional
    public void save(TimeCardCreateDto request, User user) {
        timeCardRepository.save(request.toEntity(user));
    }

}
