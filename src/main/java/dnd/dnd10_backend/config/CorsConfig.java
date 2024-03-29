package dnd.dnd10_backend.config;

import dnd.dnd10_backend.config.jwt.JwtProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 패키지명 dnd.dnd10_backend.config
 * 클래스명 CorsConfig
 * 클래스설명 cors에 대한 설정
 * 작성일 2023-01-19
 *
 * @author 원지윤
 * @version 1.0
 * [수정내용]
 * 예시) [2022-09-17] 주석추가 - 원지윤
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedOriginPattern("*");

        config.addAllowedHeader("*");
        config.addAllowedHeader(JwtProperties.AT_HEADER_STRING);
        config.addAllowedHeader(JwtProperties.RT_HEADER_STRING);

        config.addExposedHeader("*");
        config.addExposedHeader(JwtProperties.AT_HEADER_STRING);
        config.addExposedHeader(JwtProperties.RT_HEADER_STRING);

        config.addAllowedMethod("*");

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}