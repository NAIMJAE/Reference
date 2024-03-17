package kr.co.ref2.jwt;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.ref2.entity.User;
import kr.co.ref2.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// JWT Filter 설정
@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private static final String AUTH_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 요청 주소에서 마지막 문자열 추출
        String uri = request.getRequestURI();
        int i = uri.lastIndexOf("/");
        String path = uri.substring(i);

        // 토큰 추출
        // JWT 토큰은 [Authorization:Bearer CG236GSDG2.....] 이렇게 구성 됨
        String header = request.getHeader(AUTH_HEADER);
        String token = null;

        // 토큰이 있는 경우
        if (header != null && header.startsWith(TOKEN_PREFIX)){
            token = header.substring(TOKEN_PREFIX.length());
        }

        // 토큰 검사
        if (token != null){
            try {
                // 토큰의 유효성 검사
                log.info("g2g2");
                jwtProvider.validateToken(token);

                // refresh 요청 - accessToken이 만료되어 새 accessToken 발급
                if(path.equals("/refresh")){
                    log.info("JWT FILTER : 만료된 토큰");

                    Claims claims = jwtProvider.getClaims(token);
                    String uid = (String) claims.get("username");
                    String role = (String) claims.get("role");

                    User user = User.builder()
                            .uid(uid)
                            .role(role)
                            .build();

                    // 새 accessToken 토큰 생성
                    log.info("JWT FILTER : 새 access 토큰 생성");
                    String accessToken = jwtProvider.createToken(user, 1);
                    response.setStatus(HttpServletResponse.SC_CREATED);
                    response.getWriter().println(accessToken);
                    return;
                }

            }catch(JwtMyException e){
                log.info("예외");
                e.sendResponseError(response);
                return;
            }
            log.info("JWT FILTER : 토큰 인증 처리 ");

            // 토큰 인증 처리 (SecurityContextHolder에 등록)
            Authentication authentication = jwtProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("authentication : " + authentication);
        }

        log.info("JWT FILTER : 다음 필터 이동");

        // 다음 필터 이동
        filterChain.doFilter(request, response);
    }
}