package kr.co.ref2.controller;

import kr.co.ref2.dto.UserDTO;
import kr.co.ref2.entity.User;
import kr.co.ref2.jwt.JwtProvider;
import kr.co.ref2.security.MyUserDetails;
import kr.co.ref2.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO){
        log.info("login - POST");

        try {
            log.info("ControllerLogin");

            // security 인증 처리
            UsernamePasswordAuthenticationToken authToken
                    = new UsernamePasswordAuthenticationToken(userDTO.getUid(), userDTO.getPass());

            // 사용자 DB 조회
            log.info("g2");
            Authentication authentication = authenticationManager.authenticate(authToken);

            // 인증된 사용자 가져오기
            log.info("g23");
            MyUserDetails UserDetails = (MyUserDetails) authentication.getPrincipal();
            User user = UserDetails.getUser();

            // 토큰 발급(access, refresh)
            String access = jwtProvider.createToken(user, 1);   // 1일
            String refresh = jwtProvider.createToken(user, 7);  // 7일

            // refresh 토큰 DB 저장
            log.info("ControllerLogin-7 refresh 토큰 전달");

            // access 토큰 클라이언트에 전달
            Map<String, Object> map = new HashMap<>();
            map.put("grantType", "Bearer");
            map.put("access", access);
            userService.logUserAuthorities();// 사용자 권한 확인
            return ResponseEntity.ok().body(map);

        }catch (Exception e){
            log.info("login - Exception : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user not found");
        }
    }

    @GetMapping("/user")
    public ResponseEntity<List<UserDTO>> list(){
        log.info("/User - GET (service로 목록 호출) ");
        return userService.selectUsers();
    }
}
