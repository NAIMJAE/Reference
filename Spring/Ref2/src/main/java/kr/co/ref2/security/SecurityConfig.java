package kr.co.ref2.security;

import kr.co.ref2.jwt.JwtAuthenticationFilter;
import kr.co.ref2.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
// @EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private JwtProvider jwtProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        // 토큰 기반 인증 시큐리티 설정
        httpSecurity
                .csrf(CsrfConfigurer::disable)              // 사이트 위변조 방지
                .httpBasic(HttpBasicConfigurer::disable)    // 기본 HTTP 인증 박싱 비활성
                .formLogin(FormLoginConfigurer::disable)    // 폼 로그인 비활성
                .sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 비활성
                // 토큰 검색 필터 등록
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/user/login").permitAll()
                        .requestMatchers("/login").permitAll()
                        .anyRequest().permitAll()
                        //.anyRequest().authenticated()
                );
        return httpSecurity.build();
    }

    // Security 로그인 인증 암호화 인코더 설정
    @Bean
    public PasswordEncoder passwordEncoder(){
        // 같은 평문이라도 서로 다른 암호문 생성
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }
}
