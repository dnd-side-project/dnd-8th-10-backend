package dnd.dnd10_backend.board.dto.response;

import dnd.dnd10_backend.user.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 패키지명 dnd.dnd10_backend.board.dto.response
 * 클래스명 NoticeResponseDto
 * 클래스설명
 * 작성일 2023-03-03
 *
 * @author 이우진
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 * [2023-03-01] 알림 응답 dto 생성 - 이우진
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class NoticeResponseDto {

    private Long noticeId;

    private Long postId;

    private String category;

    private String title;

    private boolean checked;

    private LocalDateTime createDate;

    private String type;

    private String writerName;

    private Role writerRole;
}
