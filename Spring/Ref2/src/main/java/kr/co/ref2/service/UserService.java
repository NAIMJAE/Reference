package kr.co.ref2.service;

import kr.co.ref2.dto.UserDTO;
import kr.co.ref2.entity.User;
import kr.co.ref2.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // user2 사용자 등록
    public ResponseEntity<?> insertUser(UserDTO userDTO){
        if (userRepository.existsById(userDTO.getUid())){
            // 아이디가 이미 존재하는 경우
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(userDTO.getUid() + "already exist");
        }else{
            // 아이디가 존재하지 않는 경우 등록 진행
            // 평문 password를 암호문으로 변환
            String encoded = passwordEncoder.encode(userDTO.getPass());
            userDTO.setPass(encoded);

            User user = userDTO.toEntity();
            userRepository.save(user);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(userDTO);
        }
    }

    // user2 사용자 조회
    public ResponseEntity<?> selectUser(String uid){
        try {
            User user = userRepository.findById(uid).orElseThrow();
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(user.toDTO());
        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("user not found");
        }
    }

    // user2 목록 조회
    public ResponseEntity<List<UserDTO>> selectUsers(){
        List<UserDTO> userDTOs = userRepository.findAll()
                .stream()
                .map(entity -> UserDTO.builder()
                        .uid(entity.getUid())
                        .pass(entity.getPass())
                        .name(entity.getName())
                        .birth(entity.getBirth())
                        .age(entity.getAge())
                        .addr(entity.getAddr())
                        .role(entity.getRole())
                        .build())
                .toList();
        return ResponseEntity.ok().body(userDTOs);
    }

    // user 사용자 수정
    public ResponseEntity<?> updateUser(UserDTO userDTO){

        // 수정하기 전 존재 여부 확인
        if (userRepository.existsById(userDTO.getUid())){

            userRepository.save(userDTO.toEntity());
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(userDTO);
        }else{
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("user not found");
        }
    }

    // user 사용자 삭제
    public ResponseEntity<?> deleteUser(String uid){

        // 삭제 전 삭제할 사용자 조회
        Optional<User> optUser = userRepository.findById(uid);

        if(optUser.isPresent()){
            userRepository.deleteById(uid);
            return ResponseEntity
                    .ok()
                    .body(optUser.get());
        }else{
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("user not found");
        }
    }

    ///// 챗지피티가 준거임
    public void logUserAuthorities() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for (GrantedAuthority authority : authorities) {
            System.out.println("권한: " + authority.getAuthority());
        }
    }
}
