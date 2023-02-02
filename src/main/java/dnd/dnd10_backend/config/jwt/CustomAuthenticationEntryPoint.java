package dnd.dnd10_backend.config.jwt;

import dnd.dnd10_backend.common.domain.CustomerErrorResponse;
import dnd.dnd10_backend.common.domain.enums.CodeStatus;
import dnd.dnd10_backend.common.domain.enums.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * 패키지명 dnd.dnd10_backend.config.jwt
 * 클래스명 CustomAuthenticationEntryPoint
 * 클래스설명 토큰이 만료되었을 때 호출하는 class
 * 작성일 2023-01-19
 * 
 * @author 원지윤
 * @version 1.0
 * [수정내용] 
 * 예시) [2022-09-17] 주석추가 - 원지윤
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String exception = (String) request.getAttribute(JwtProperties.AT_HEADER_STRING);

        if(exception.equals("토큰이 만료되었습니다.")) {
            setResponse (response, CodeStatus.ACCESS_TOKEN_EXPIRED);
        }

        if(exception.equals("유효하지 않은 토큰입니다.")) {
            setResponse(response, CodeStatus.INVALID_TOKEN);
        }
    }

    private void setResponse(HttpServletResponse response, CodeStatus codeStatus) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println("{ \"status\" : \"" + ResponseStatus.ERROR
                + "\",\n \"code\" : " +  codeStatus.getCode()
                + ",\n \"message\" : \"" + codeStatus.getMessage()
                + "\",\n \"timeStamp\" : \"" + System.currentTimeMillis()+ "\"}");
        response.getWriter().flush();
        response.getWriter().close();

    }
}