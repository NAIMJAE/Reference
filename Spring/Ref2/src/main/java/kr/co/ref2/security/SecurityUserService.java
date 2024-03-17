package kr.co.ref2.security;

import kr.co.ref2.entity.User;
import kr.co.ref2.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service
public class SecurityUserService implements UserDetailsService {
    // 주입
    private final UserRepository userRepository;

    // 인증 수행
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("securityUserService 시작");
        
        Optional<User> result = userRepository.findById(username);
        log.info("securityUserService-1 DB에서 user 조회");
        log.info("securityUserService-2 result : "+result.toString());
        UserDetails userDetails = null;

        if(!result.isEmpty()){
            log.info("securityUserService-3 사용자가 존재하는 경우");

            // 해당하는 사용자가 존재하면 인증 객체 생성
            userDetails = MyUserDetails.builder()
                    .user(result.get())
                    .build();
            log.info("securityUserService-4 userDetails : " + userDetails.toString());
        }

        log.info("securityUserService 끝");
        // Security ContextHolder에 저장
        return userDetails;
    }
}
