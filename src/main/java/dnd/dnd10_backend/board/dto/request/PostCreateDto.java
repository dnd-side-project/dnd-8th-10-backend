package dnd.dnd10_backend.board.dto.request;

import dnd.dnd10_backend.board.domain.Post;
import dnd.dnd10_backend.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 패키지명 dnd.dnd10_backend.board.dto.request
 * 클래스명 PostCreateDto
 * 클래스설명
 * 작성일 2023-02-28
 *
 * @author 이우진
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 * [2023-02-28] 게시글 작성 dto - 이우진
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PostCreateDto {

    private String title;

    private String content;

    private String category;

    //이미지 추가해야함

    public Post toEntity(User user) {
        return Post.builder()
                .title(title)
                .content(content)
                .category(category)
                .userCode(user.getUserCode())
                .userName(user.getUsername())
                .role(user.getRole())
                .build();
    }
}
