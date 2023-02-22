package dnd.dnd10_backend.board.domain;

import com.sun.istack.NotNull;
import dnd.dnd10_backend.user.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 패키지명 dnd.dnd10_backend.board.domain
 * 클래스명 Post
 * 클래스설명
 * 작성일 2023-02-21
 *
 * @author 이우진
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 * [2023-02-21] 엔티티 초안 작성 - 이우진
 */

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String title;

    @NotNull
    @Lob
    private String content;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private int viewCount; //조회수, int 형은 default 0

    @Column(columnDefinition = "integer default 0", nullable = false)
    private int checkCount; //좋아요 수

    private String category;

    private Long userCode;

    private String userName;

    private Role role;

    //BaseTimeEntity로 만들어서 상속받아서 사용할지 고민
//    @CreatedDate
//    @Column(updatable = false)
//    private LocalDateTime createDate;
//
//    @LastModifiedDate
//    private LocalDateTime modifiedDate;

    //이미지 첨부 기능 추가

    public Post updateView(int viewCount) {
        this.viewCount = viewCount + 1;
        return this;
    }

    //좋아요 ++
    public Post plusCheck(int checkCount) {
        this.checkCount = checkCount + 1;
        return this;
    }

    //좋아요 취소
    public Post minusCheck(int checkCount) {
        this.checkCount = checkCount - 1;
        return this;
    }

    public void update(String title, String content, String category) {
        this.title = title;
        this.content = content;
        this.category = category;
    }
}
