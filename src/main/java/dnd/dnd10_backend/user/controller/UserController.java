package dnd.dnd10_backend.user.controller;

import dnd.dnd10_backend.common.domain.SingleResponse;
import dnd.dnd10_backend.common.domain.enums.CodeStatus;
import dnd.dnd10_backend.common.service.ResponseService;
import dnd.dnd10_backend.config.jwt.JwtProperties;
import dnd.dnd10_backend.user.dto.request.UserSaveRequestDto;
import dnd.dnd10_backend.user.dto.response.UserResponseDto;
import dnd.dnd10_backend.user.service.TokenService;
import dnd.dnd10_backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 패키지명 dnd.dnd10_backend.user.controller
 * 클래스명 UserController
 * 클래스설명
 * 작성일 2023-01-18
 *
 * @author 원지윤
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 * [2023-01-31] 프론트에 토큰 관련 헤더 노출 - 원지윤
 * [2023-02-02] 사용자 정보 조회, 등록 , 수정 api 추가 - 원지윤
 * [2023-02-02] 토큰 관련 컨트롤러 분리 - 원지윤
 * [2023-02-03] 토큰에서 이메일 식별 - 원지윤
 * [2023-02-06] #50 User 엔티티 userResponseDto로 변환 - 이우진
 * [2023-02-20] 사용자 삭제 api 추가 - 원지윤
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final ResponseService responseService;
    private final TokenService tokenService;

    /**
     * 사용자 정보 조회 api
     * @param
     * @return
     */
    @GetMapping("/user")
    public ResponseEntity getUser(HttpServletRequest request) {
        String token = request.getHeader(JwtProperties.AT_HEADER_STRING)
                        .replace(JwtProperties.TOKEN_PREFIX,"");
        UserResponseDto userResponseDto = userService.findUser(token);
        SingleResponse<UserResponseDto> response = responseService.getResponse(userResponseDto,
                                                                CodeStatus.SUCCESS_SEARCHED_USER);
        return ResponseEntity.ok().body(response);
    }

    /**
     * 사용자 등록 api
     * @param requestDto
     * @return
     */
    @PostMapping("/user/signup")
    public ResponseEntity signupUser(@RequestBody UserSaveRequestDto requestDto,
                                     HttpServletRequest request) {
        String token = request.getHeader(JwtProperties.AT_HEADER_STRING)
                            .replace(JwtProperties.TOKEN_PREFIX,"");
        UserResponseDto userResponseDto = userService.saveUser(requestDto, token);
        SingleResponse<UserResponseDto> response = responseService.getResponse(userResponseDto,
                                                                CodeStatus.SUCCESS_CREATED_USER);
        return ResponseEntity.ok().body(response);
    }

    /**
     * 사용자 정보 업데이트 api
     * @param requestDto
     * @return
     */
    @PutMapping("/user/update")
    public ResponseEntity updateUser(@RequestBody UserSaveRequestDto requestDto,
                                     HttpServletRequest request) {
        String token = request.getHeader(JwtProperties.AT_HEADER_STRING)
                            .replace(JwtProperties.TOKEN_PREFIX,"");
        UserResponseDto userResponseDto = userService.updateUser(requestDto, token);
        SingleResponse<UserResponseDto> response = responseService.getResponse(userResponseDto,
                                                                    CodeStatus.SUCCESS_UPDATED_USER);
        return ResponseEntity.ok().body(response);
    }

    /**
     * 사용자 삭제 api
     * @param request
     * @return
     */
    @DeleteMapping("/user")
    public ResponseEntity deleteUser(HttpServletRequest request) {
        String token = request.getHeader(JwtProperties.AT_HEADER_STRING)
                .replace(JwtProperties.TOKEN_PREFIX,"");

        userService.deleteUser(token);

        SingleResponse<String> response = responseService.getResponse("",CodeStatus.SUCCESS_DELETED_USER);

        return ResponseEntity.ok().body(response);
    }

    /**
     * 로그아웃 api
     * @param request
     * @param response
     * @param session
     * @return
     */
    @GetMapping("/user/logout")
    public ResponseEntity logout(HttpServletRequest request,
                                 HttpServletResponse response,
                                 HttpSession session){
        String token = request.getHeader(JwtProperties.AT_HEADER_STRING)
                .replace(JwtProperties.TOKEN_PREFIX,"");

        String refreshToken = request.getHeader(JwtProperties.RT_HEADER_STRING)
                .replace(JwtProperties.TOKEN_PREFIX,"");

        userService.getLogout(token);


        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        //카카오 세션 로그아웃웃
        SingleResponse singleResponse = responseService.getResponse("",CodeStatus.SUCCESS);

        return ResponseEntity.ok().body(singleResponse);
    }
}
