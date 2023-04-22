package dnd.dnd10_backend.store.service;

import dnd.dnd10_backend.store.domain.Store;
import dnd.dnd10_backend.store.dto.response.StoreResponseDto;
import dnd.dnd10_backend.store.repository.StoreRepository;
import dnd.dnd10_backend.user.domain.User;
import dnd.dnd10_backend.user.dto.response.UserResponseDto;
import dnd.dnd10_backend.user.dto.response.UserStoreResponseDto;
import dnd.dnd10_backend.user.repository.UserRepository;
import dnd.dnd10_backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 패키지명 dnd.dnd10_backend.store.service
 * 클래스명 StoreService
 * 클래스설명
 * 작성일 2023-02-17
 *
 * @author 원지윤
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 */
@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    /**
     *  사용자가 포함되어있는 store정보를 조회하는 메소드
     * @param token access token
     * @return 응답해주려는 store 정보
     */
    public StoreResponseDto getStoreInfo(final String token){
        User user = userService.getUserByEmail(token);

        Store store = user.getStore();

        return StoreResponseDto.of(store, convertUserToDto(userRepository.findByStore(store)));
    }

    /**
     * List<User>를 List<UserStoreResponseDto> 변환해주는 메소드
     * @param userList List타입의 user목록
     * @return List타입의 UserStoreResponseDto목록
     */
    public List<UserStoreResponseDto> convertUserToDto(List<User> userList){
        List<UserStoreResponseDto> responseDtoList = userList.stream()
                .map(t -> UserStoreResponseDto.of(t))
                .collect(Collectors.toList());
        return responseDtoList;
    }
}
