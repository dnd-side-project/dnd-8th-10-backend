package dnd.dnd10_backend.board.domain;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 패키지명 dnd.dnd10_backend.board.domain
 * 클래스명 Image
 * 클래스설명
 * 작성일 2023-03-02
 *
 * @author 원지윤
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 */
@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Image {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @NotNull
    private String originalFileName;

    @NotNull
    private String savedFileName;

    @NotNull
    private String extension;
    @NotNull
    private String storedFilePath;

    private long fileSize;
}



