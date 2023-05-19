package dnd.dnd10_backend.user.service;

import dnd.dnd10_backend.user.domain.User;
import dnd.dnd10_backend.user.repository.UserCacheRepository;
import dnd.dnd10_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 패키지명 dnd.dnd10_backend.user.service
 * 클래스명 CustomUserDetailsService
 * 클래스설명
 * 작성일 2023-02-03
 *
 * @author 원지윤
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserCacheRepository userCacheRepository;
    @Override
    public UserDetails loadUserByUsername(String kakaoEmail) throws UsernameNotFoundException {
        return userCacheRepository.findByEmail(kakaoEmail);
    }
}
