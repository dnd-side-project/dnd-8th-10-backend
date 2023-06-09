package dnd.dnd10_backend.user.service;

import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dnd.dnd10_backend.Inventory.service.DefaultInventoryService;
import dnd.dnd10_backend.checkList.service.DefaultCheckListService;
import dnd.dnd10_backend.common.domain.enums.CodeStatus;
import dnd.dnd10_backend.common.exception.CustomerNotFoundException;
import dnd.dnd10_backend.config.jwt.JwtProperties;
import dnd.dnd10_backend.store.domain.Store;
import dnd.dnd10_backend.store.repository.StoreRepository;
import dnd.dnd10_backend.user.domain.User;
import dnd.dnd10_backend.user.dto.request.UserSaveRequestDto;
import dnd.dnd10_backend.user.dto.response.UserCreateResponseDto;
import dnd.dnd10_backend.user.dto.response.UserResponseDto;
import dnd.dnd10_backend.user.repository.UserCacheRepository;
import dnd.dnd10_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.List;

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
 * [2023-02-20] 사용자 삭제에 대한 메소드 추가 - 원지윤
 * [2023-02-24] 사용자 생성 시 발생하는 에러 해결 - 원지윤
 * [2023-02-24] 사용자 등록과 업데이트 함수 분리 - 원지윤
 * [2023-03-01] 카카오 소셜 로그아웃 추가 - 원지윤
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserCacheRepository userCacheRepository;
    private final StoreRepository storeRepository;
    private final DefaultInventoryService defaultInventoryService;
    private final DefaultCheckListService defaultCheckListService;

    @Value("${spring.security.oauth2.client.registration.kakao.app-key-admin}")
    String app_key_admin;

    /**
     * 토큰 정보로 사용자를 조회하는 메소드
     * @param token access token
     * @return
     */
    public UserCreateResponseDto getUserByToken(final String token){
        Long userCode = require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token)
                .getClaim("id").asLong();
        User user = userRepository.findByUserCode(userCode);
        if(user == null) throw new CustomerNotFoundException(CodeStatus.NOT_FOUND_USER);

        return UserCreateResponseDto.of(user);
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
        User user = userCacheRepository.findByEmail(email);
        if(user == null) throw new CustomerNotFoundException(CodeStatus.NOT_FOUND_USER);

        return user;
    }

    /**
     * 사용자 등록하는 메소드
     * @param requestDto client한테서 받아온 사용자 정보
     * @return 응답해주려는 user 정보
     */
    public UserResponseDto saveUser(UserSaveRequestDto requestDto, User user){

        Store store = storeRepository.findStoreByStoreName(requestDto.getWorkPlace());

        if(store == null){
            store = Store.builder()
                    .storeName(requestDto.getWorkPlace())
                    .storeLocation(requestDto.getWorkLocation())
                    .build();

            store = storeRepository.save(store);
            defaultInventoryService.saveDafaultInventories(store);
        }

        int count = userRepository.findByStore(store).size();

        //requestDto로 user 정보 update
        user.updateUser(requestDto, store);

        //List<User> userList = userRepository.findByKakaoNickname(user.getUsername());

        user.setUserProfileCode((count%10)+1);

        //user 정보 저장
        userRepository.save(user);

        //사용자 출근요일에 기본 체크리스트 추가
        defaultCheckListService.saveFirstDefaultCheckList(user);

        return UserResponseDto.of(user,store);
    }

    /**
     * 사용자 정보를 업데이트하는 함수
     * @param requestDto 업데이트 하려는 정보
     * @param user 업데이트하려는 사용자
     * @return 응답해주려는 user 정보
     */
    public UserResponseDto updateUser(UserSaveRequestDto requestDto, User user){

        Store store = user.getStore();
        //requestDto로 user 정보 update
        user.updateUser(requestDto, store);

        //캐시 내용 업데이트 및 DB업데이트 내용 저장
        user = userCacheRepository.updateUser(user);

        //사용자 출근요일에 기본 체크리스트 추가
        defaultCheckListService.saveFirstDefaultCheckList(user);

        return UserResponseDto.of(user,store);
    }

    /**
     * user를 삭제하는 메소드
     * @param user 삭제하려는 user
     */
    @CacheEvict(value = "userCacheStore", key="#user.kakaoEmail")
    public void deleteUser(User user){
        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        headers.set("Authorization", "KakaoAK " + app_key_admin);

        // HttpBody 오브젝트 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("target_id_type", "user_id");
        params.add("target_id", user.getKakaoId().toString());

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(params, headers);

        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v1/user/unlink",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        log.info("status code : {}",response.getStatusCode());

        if(response.getStatusCode() == HttpStatus.OK) {
            userRepository.delete(user);
            return;
        }

        throw new CustomerNotFoundException("카카오 탈퇴 도중 오류 발생");

    }

    /**
     * 카카오 소셜 로그아웃
     * @param user 로그아웃 하려는 사용자
     */
    @CacheEvict(value = "userCacheStore", key="#user.kakaoEmail")
    public void getLogout(User user) {
        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        headers.set("Authorization", "KakaoAK " + app_key_admin);

        // HttpBody 오브젝트 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("target_id_type", "user_id");
        params.add("target_id", user.getKakaoId().toString());

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(params, headers);

        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v1/user/logout",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        if(response.getStatusCode() != HttpStatus.OK)
                throw new CustomerNotFoundException("카카오 로그아웃 도중 오류 발생");

    }

    /**
     * 사용자를 정보를 찾는 메소드
     * @param user 사용자
     * @return
     */
    public UserResponseDto findUser(User user){
        return UserResponseDto.of(user,user.getStore());
    }

    /**
     * userCode를 통해 user를 찾는 메소드
     * @param userCode
     * @return userCode로 찾은 User정보
     */
    public User findByUserCode(Long userCode) {
        return userRepository.findByUserCode(userCode);
    }


}