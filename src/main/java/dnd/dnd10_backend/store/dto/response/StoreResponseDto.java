package dnd.dnd10_backend.store.dto.response;

import dnd.dnd10_backend.store.domain.Store;
import dnd.dnd10_backend.user.dto.response.UserStoreResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 패키지명 dnd.dnd10_backend.store.dto.response
 * 클래스명 StoreResponseDto
 * 클래스설명
 * 작성일 2023-02-17
 *
 * @author 원지윤
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StoreResponseDto {
    private String storeName;
    private String storeLocation;
    List<UserStoreResponseDto> UserList;

    public static StoreResponseDto of(Store store, List<UserStoreResponseDto> userList){
        return new StoreResponseDto(
                store.getStoreName(),
                store.getStoreLocation(),
                userList
        );
    }
}
