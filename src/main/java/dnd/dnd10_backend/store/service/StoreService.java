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

    public StoreResponseDto getStoreInfo(final String token){
        User user = userService.getUserByEmail(token);

        Store store = user.getStore();

        return StoreResponseDto.of(store, convertUserToDto(userRepository.findByStore(store)));
    }

    public List<UserStoreResponseDto> convertUserToDto(List<User> userList){
        List<UserStoreResponseDto> responseDtoList = new ArrayList<>();
        for(User user: userList){
            responseDtoList.add(UserStoreResponseDto.of(user));
        }
        return responseDtoList;
    }
}
