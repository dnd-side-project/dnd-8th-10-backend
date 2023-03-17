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
import dnd.dnd10_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
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
public class UserService {

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final DefaultInventoryService defaultInventoryService;
    private final DefaultCheckListService defaultCheckListService;

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
                    .storeLocation(requestDto.getWorkLocation())
                    .build();

            store = storeRepository.save(store);
            defaultInventoryService.saveDafaultInventories(store);
        }

        int count = userRepository.findByStore(store).size();

        //requestDto로 user 정보 update
        user.updateUser(requestDto, store);

        List<User> userList = userRepository.findByKakaoNickname(user.getUsername());

        user.setUserProfileCode((count%10)+1);

        //user 정보 저장
        userRepository.save(user);

        //사용자 출근요일에 기본 체크리스트 추가
        defaultCheckListService.saveFirstDefaultCheckList(user);

        return UserResponseDto.of(user,store);
    }

    /**
     * 사용자 정보를 업데이트하는 함수
     * @param requestDto
     * @param token
     * @return
     */
    public UserResponseDto updateUser(UserSaveRequestDto requestDto, final String token){
        //user 찾기
        User user = getUserByEmail(token);

        Store store = storeRepository.findStoreByStoreName(requestDto.getWorkPlace());

        if(store == null){
            store = Store.builder()
                    .storeName(requestDto.getWorkPlace())
                    .storeLocation(requestDto.getWorkLocation())
                    .build();

            store = storeRepository.save(store);
            defaultInventoryService.saveDafaultInventories(store);
        }

        //requestDto로 user 정보 update
        user.updateUser(requestDto, store);

        //user 정보 저장
        userRepository.save(user);

        //사용자 출근요일에 기본 체크리스트 추가
        defaultCheckListService.saveFirstDefaultCheckList(user);

        return UserResponseDto.of(user,store);
    }

    /**
     * user를 삭제하는 메소드
     * @param token
     */
    public void deleteUser(final String token, final String kakaoToken){
        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        headers.set("Authorization", "Bearer " + kakaoToken);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(null, headers);

        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v1/user/unlink",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        System.out.println(response.getStatusCode());

        if(response.getStatusCode() == HttpStatus.OK) {
            User user = getUserByEmail(token);
            userRepository.delete(user);
        }

        throw new CustomerNotFoundException("카카오 탈퇴 도중 오류 발생");

    }

    /**
     * 카카오 소셜 로그아웃
     * @param kakaoToken 카카오 access token
     */
    public void getLogout(final String kakaoToken) {
        String reqURL ="https://kapi.kakao.com/v1/user/logout";

        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        headers.set("Authorization", "Bearer " + kakaoToken);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(null, headers);

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

    /**
     * userCode를 통해 user를 찾는 메소드
     * @param userCode
     * @return
     */
    public User findByUserCode(Long userCode) {
        User user = userRepository.findByUserCode(userCode);
        return user;
    }


}