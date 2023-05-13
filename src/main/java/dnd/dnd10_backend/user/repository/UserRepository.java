package dnd.dnd10_backend.user.repository;

import dnd.dnd10_backend.store.domain.Store;
import dnd.dnd10_backend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 패키지명 dnd.dnd10_backend.user.repository
 * 클래스명 UserRepository
 * 클래스설명
 * 작성일 2023-01-18
 *
 * @author 원지윤
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 */
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select distinct(u) from User u join fetch u.store where u.kakaoEmail = :email")
    public User findByKakaoEmail(@Param("email")String kakaoEmail);

    public List<User> findByStore(Store store);

    @Query("select distinct(u) from User u join fetch u.store where u.userCode = :userCode")
    public User findByUserCode(@Param("userCode") Long userCode);

    public User findByKakaoId(Long id);

    public List<User> findByKakaoNickname(String userName);

    public User findByKakaoNicknameAndUserProfileCode(String userName, int userProfileCode);

}
