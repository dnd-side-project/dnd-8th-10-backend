package dnd.dnd10_backend.config.jwt;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import dnd.dnd10_backend.user.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.auth0.jwt.JWT.require;


/**
 * 패키지명 dnd.dnd10_backend.config.jwt
 * 클래스명 JwtRequestFilter
 * 클래스설명
 * 작성일 2023-01-19
 *
 * @author 원지윤
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final TokenService tokenService;

    public JwtRequestFilter(TokenService tokenService){
        this.tokenService = tokenService;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String jwtHeader = request.getHeader(JwtProperties.AT_HEADER_STRING);

        if(jwtHeader == null || !jwtHeader.startsWith(JwtProperties.TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = jwtHeader.replace(JwtProperties.TOKEN_PREFIX, "");
        Long userCode = null;

        try {
            userCode = require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token)
                    .getClaim("id").asLong();
            Authentication authentication = tokenService.getAuthentication(userCode);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (TokenExpiredException e) {
            e.printStackTrace();
            request.setAttribute(JwtProperties.AT_HEADER_STRING, "토큰이 만료되었습니다.");
        } catch (JWTVerificationException e) {
            e.printStackTrace();
            request.setAttribute(JwtProperties.AT_HEADER_STRING, "유효하지 않은 토큰입니다.");
        }

        request.setAttribute("userCode", userCode);

        filterChain.doFilter(request, response);
    }
}
