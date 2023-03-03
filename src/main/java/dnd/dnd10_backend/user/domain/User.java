package dnd.dnd10_backend.user.domain;

import dnd.dnd10_backend.board.domain.Notice;
import dnd.dnd10_backend.calendar.domain.TimeCard;
import dnd.dnd10_backend.checkList.domain.CheckList;
import dnd.dnd10_backend.store.domain.Store;
import dnd.dnd10_backend.user.domain.enums.Role;
import dnd.dnd10_backend.user.dto.request.UserSaveRequestDto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 패키지명 dnd.dnd10_backend.user.domain
 * 클래스명 User
 * 클래스설명
 * 작성일 2023-01-18
 *
 * @author 원지윤
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 * [2023-01-28] user 역할,근무시간,근무장소 추가 - 원지윤
 * [2023-02-02] user 휴대전화 번호 추가 - 원지윤
 * [2023-02-06] timecard 연관관계 매핑 추가 - 이우진
 * [2023-02-08] 카카오 프로필 삭제 - 원지윤
 * [2023-02-10] store연관 관계 추가 - 원지윤
 * [2023-02-11] workPlace 삭제
 * [2023-02-20] 체크리스트에 대한 연관 관계 추가 - 원지윤
 * [2023-02-20] timecard 연관 관계 삭제 - 이우진
 * [2023-02-24] 토큰 연관관계 추가 - 원지윤
 */
@Entity
@Data
@NoArgsConstructor
@Table(name = "user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_code")
    private Long userCode;

    @Column(name = "kakao_id")
    private Long kakaoId;

    @Column(name = "kakao_nickname")
    private String kakaoNickname;

    @Column(name = "kakao_email")
    private String kakaoEmail;

    @Column(name = "user_profile")
    private int userProfileCode;

    @Column(name = "work_role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "work_time")
    private String workTime;

    @Column(name = "wage")
    private double wage;

    @Column(name = "user_role")
    private String userRole;

    @Column(name = "create_time")
    @CreationTimestamp
    private Timestamp createTime;

    @ManyToOne
    @JoinColumn(name = "store_idx", insertable = false)
    private Store store;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<CheckList> CheckList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Notice> Notice = new ArrayList<>();

    @Builder
    public User(Long kakaoId, String kakaoNickname,
                String kakaoEmail, String userRole, double wage) {

        this.kakaoId = kakaoId;
        this.kakaoNickname = kakaoNickname;
        this.kakaoEmail = kakaoEmail;
        this.userRole = userRole;
        this.wage = wage;
    }

    @Builder(builderMethodName = "dtoBuilder")
    public void updateUser(UserSaveRequestDto requestDto, Store store) {
        this.role = requestDto.getRole();
        this.phoneNumber = requestDto.getPhoneNumber();
        this.workTime = requestDto.getWorkTime();
        this.wage = requestDto.getWage();
        this.store = store;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> auth = new ArrayList<GrantedAuthority>();
        auth.add(new SimpleGrantedAuthority(userRole));
        return auth;
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return kakaoNickname;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}