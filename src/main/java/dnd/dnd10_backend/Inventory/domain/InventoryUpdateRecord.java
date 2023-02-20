package dnd.dnd10_backend.Inventory.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import dnd.dnd10_backend.Inventory.domain.enums.Category;
import dnd.dnd10_backend.Inventory.dto.request.UpdateInventoryRequestDto;
import dnd.dnd10_backend.calendar.domain.TimeCard;
import dnd.dnd10_backend.store.domain.Store;
import dnd.dnd10_backend.user.domain.User;
import dnd.dnd10_backend.user.domain.enums.Role;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 패키지명 dnd.dnd10_backend.Inventory.domain
 * 클래스명 InventoryUpdateRecord
 * 클래스설명 시재 업데이트 이력에 대한 entity
 * 작성일 2023-02-11
 *
 * @author 원지윤
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 */
@Entity
@Data
@NoArgsConstructor
@Table(name = "inventory_record")
public class InventoryUpdateRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_idx")
    private Long recordIdx;

    @Column(name = "inventory_name")
    private String inventoryName;

    @Column(name = "diff")
    private int diff;

    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "work_role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "work_day")
    private String workDay;

    @Column(name = "work_time")
    private String workTime;

    @Column(name = "user_profile_code")
    private int userProfileCode;

    @ManyToOne
    @JoinColumn(name = "store_idx")
    private Store store;

    @ManyToOne
    @JoinColumn(name = "id")
    private TimeCard timeCard;

    @CreationTimestamp
    private LocalDateTime createTime;

    @Builder
    public InventoryUpdateRecord(Inventory inventory, int diff, Category category, User user, Store store, TimeCard timeCard){
        this.inventoryName = inventory.getInventoryName();
        this.diff = diff;
        this.category = category;
        this.userName = user.getUsername();
        this.role = user.getRole();
        this.workTime = timeCard.getWorkTime();
        this.userProfileCode = user.getUserProfileCode();
        this.store = store;
        this.timeCard = timeCard;
    }
}
