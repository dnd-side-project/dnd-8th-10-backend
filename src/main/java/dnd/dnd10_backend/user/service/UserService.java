package dnd.dnd10_backend.user.service;

import com.auth0.jwt.algorithms.Algorithm;
import dnd.dnd10_backend.common.domain.enums.CodeStatus;
import dnd.dnd10_backend.common.exception.CustomerNotFoundException;
import dnd.dnd10_backend.config.jwt.JwtProperties;
import dnd.dnd10_backend.store.domain.Store;
import dnd.dnd10_backend.store.repository.StoreRepository;
import dnd.dnd10_backend.user.domain.User;
import dnd.dnd10_backend.user.dto.request.UserSaveRequestDto;
import dnd.dnd10_backend.user.dto.response.UserResponseDto;
import dnd.dnd10_backend.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.auth0.jwt.JWT.require;

/**
 * 패키지명 dnd.dnd10_backend.user.service
 * 클래스명 UserService
 * 클래스설명
 * 작성일 2023-01-18
 *
 * @author 원지윤
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 * [2023-01-21] refresh token에 대한 내용 수정 - 원지윤
 * [2023-02-02] token관련 service 분리 - 원지윤
 * [2023-02-06] getUserByToken 오타 수정 - 이우진
 * [2023-02-06] getUserByEmail 리턴값 엔티티로 수정 - 이우진
 * [2023-02-13] findByUserCode 추가 - 이우진
 */
@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    StoreRepository storeRepository;

    /**
     * 토큰 정보로 사용자를 조회하는 메소드
     * @param token access token
     * @return
     */
    public UserResponseDto getUserByToken(final String token){
        Long userCode = require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token)
                .getClaim("id").asLong();
        User user = userRepository.findByUserCode(userCode);
        if(user == null) throw new CustomerNotFoundException(CodeStatus.NOT_FOUND_USER);


        return UserResponseDto.of(user, findStoreNameByUser(user));
    }

    /**
     * 이메일로 사용자를 조회하는 메소드
     * @param token access token
     * @return
     */
    public User getUserByEmail(final String token){
        String email = require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token)
                .getClaim("email").asString();
        //user 찾기
        User user = userRepository.findByKakaoEmail(email);
        if(user == null) throw new CustomerNotFoundException(CodeStatus.NOT_FOUND_USER);

        return user;
    }

    /**
     * 사용자 등록하는 메소드
     * @param requestDto client한테서 받아온 사용자 정보
     * @return UserResponseDto
     */
    public UserResponseDto saveUser(UserSaveRequestDto requestDto, final String token){
        //user 찾기
        User user = getUserByEmail(token);

        Store store = storeRepository.findStoreByStoreName(requestDto.getWorkPlace());

        if(store == null){
            store = Store.builder()
                    .storeName(requestDto.getWorkPlace())
                    .build();

            storeRepository.save(store);
        }

        int count = userRepository.findByStore(store).size();

        //requestDto로 user 정보 update
        user.updateUser(requestDto);

        user.setStore(store);
        user.setUserProfileCode((count%10)+1);

        //user 정보 저장
        userRepository.save(user);

        return UserResponseDto.of(user,store);
    }

    /**
     * access token으로 사용자를 찾는 메소드
     * @param token access token
     * @return
     */
    public UserResponseDto findUser(final String token){
        User user = getUserByEmail(token);
        Store store = findStoreNameByUser(user);
        return UserResponseDto.of(user,store);
    }

    /**
     * 사용자 정보로 매장 정보를 찾는 메소드
     * @param user
     * @return
     */
    public Store findStoreNameByUser(User user){
        Store store = storeRepository.findStoreByStoreIdx(user.getStore().getStoreIdx());
        if(store == null) throw new CustomerNotFoundException();
        return store;
    }

    public User findByUserCode(Long userCode) {
        User user = userRepository.findByUserCode(userCode);
        return user;
    }

}