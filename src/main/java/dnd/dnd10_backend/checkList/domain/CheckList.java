package dnd.dnd10_backend.checkList.domain;

import dnd.dnd10_backend.checkList.dto.request.CheckListRequestDto;
import dnd.dnd10_backend.checkList.dto.request.UpdateCheckListRequestDto;
import dnd.dnd10_backend.common.domain.BaseTimeEntity;
import dnd.dnd10_backend.user.domain.User;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * 패키지명 dnd.dnd10_backend.checkList.domain
 * 클래스명 CheckList
 * 클래스설명
 * 작성일 2023-02-05
 *
 * @author 원지윤
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 * [2023-02-13] column애 대한 nullable 설정 및 default 지정 - 원지윤
 */
@Entity
@Data
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class) // 추가
@Table(name = "check_list")
public class CheckList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "check_idx")
    private Long checkIdx;

    @Column(name = "check_date",nullable = false)
    private LocalDate date;

    @Column(name = "content")
    private String content;

    @Column(name = "status",nullable = false)
    @ColumnDefault("N")
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_code")
    private User user;

    @CreatedDate
    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;

    @Column(name = "checked_time")
    private LocalDateTime checkedTime;

    @Builder
    public CheckList(LocalDate date, String content, String status, User user) {
        this.date = date;
        this.content = content;
        this.status = status;
        this.user = user;
        this.checkedTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
    }

    public CheckList(CheckListRequestDto requestDto, User user) {
        this.date = requestDto.getDate();
        this.content = requestDto.getContent();
        this.status = requestDto.getStatus();
        this.user = user;
    }

    public void update(UpdateCheckListRequestDto requestDto){
        this.content = requestDto.getContent();
        this.status = requestDto.getStatus();
    }
}
