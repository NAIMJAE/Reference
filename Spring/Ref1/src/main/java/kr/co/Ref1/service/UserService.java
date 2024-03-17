package kr.co.Ref1.service;

import kr.co.Ref1.dto.UserDTO;
import kr.co.Ref1.entity.User;
import kr.co.Ref1.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    /*
        JPA save()는 삽입, 수정을 동시에 할 수 있는 메서드 이기 때문에
        삽입을 수행하고자 할 경우에는 먼저 미리 existsById()로 존재여부를 확인하고
        save()를 수행하면 됨

        ResponseEntity의 주요 속성
        - body: 응답의 본문을 나타내는 속성, 반환하고자 하는 데이터를 포함.
                주로 객체 형태로 제공되며, JSON 형식으로 변환되어 클라이언트에게 반환.

        - headers: 응답에 포함될 HTTP 헤더를 정의하는 속성.
                   헤더는 추가적인 메타데이터를 제공하고, 클라이언트와 서버 간의 통신을 지원.
                   예를 들어 캐싱 정책, 인증 정보, 쿠키 등을 설정.

        - status: HTTP 응답 상태 코드를 나타내는 속성.
                  주로 성공(200), 생성됨(201), 잘못된 요청(400), 권한 없음(403), 리소스가 없음(404) 등의 상태 코드를 설정.

        - HttpStatus : HTTP 응답 상태 코드를 나타내는 데 사용
                       OK (200): 요청이 성공적으로 처리
                       CREATED (201): 요청이 성공적으로 처리되어 새로운 리소스가 생성.
                       BAD_REQUEST (400): 요청이 잘못되었거나 구문 오류가 있음.
                       UNAUTHORIZED (401): 요청에 대한 권한이 없음.
                       FORBIDDEN (403): 요청이 서버에 의해 거부되.
                       NOT_FOUND (404): 요청한 리소스를 찾을 수 없음.

        - statusCode: 응답 상태 코드를 설정하는 메소드. status(HttpStatus) 메소드를 사용하여 상태 코드를 설정.

        - statusText: 응답 상태 코드에 대한 설명을 나타내는 속성.
                      일반적으로는 HTTP 상태 코드와 함께 전달되며, 클라이언트가 응답을 이해하는 데 도움.

        - contentType: 응답 본문의 컨텐츠 타입을 나타내는 속성.
                       주로 "application/json", "text/html" 등의 MIME 타입을 설정하여 클라이언트가 본문을 올바르게 해석할 수 있도록 함.
    */

    // User 등록
    public ResponseEntity<?> insertUser(UserDTO userDTO){
        log.info("insertUser");

        if (userRepository.existsById(userDTO.getUid())){
            // 아이디가 존재하는 경우 (중복)
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(userDTO.getUid() + "already exist");
        }else {
            // 아이디가 존재하지 않는 경우 등록 진행
            User user = userDTO.toEntity();
            userRepository.save(user);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(userDTO);
        }
    }
    
    // User 정보 조회
    // try ~ catch 를 이용한 User 조회 방법 (User가 없으면 예외 발생)
    public ResponseEntity<?> selectUser(String uid){
        log.info("selectUser");

        try {
            // orElseThrow() 메서드로 존재하는 Entity (User 정보) 가져오기
            User user = userRepository.findById(uid).orElseThrow();

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(user.toDTO());
        }catch (Exception e){
            // User가 존재하지 않는 경우 예외 발생, NOT_FOUND 응답데이터 전송
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("user not found");
        }
    }

    // User 목록 조회
    // findAll() 메서드로 User목록 조회 후 stream을 이용하여 List로 변환 후 반환
    public ResponseEntity<List<UserDTO>> selectUsers(){
         log.info("selectUsers");

         List<UserDTO> userDTOs = userRepository.findAll()
                                                .stream()
                                                .map(entity -> UserDTO.builder()
                                                        .uid(entity.getUid())
                                                        .name(entity.getName())
                                                        .birth(entity.getBirth())
                                                        .age(entity.getAge())
                                                        .addr(entity.getAddr())
                                                        .build())
                                                .toList();
         return ResponseEntity.ok().body(userDTOs);
    }

    // User 정보 수정
    // User 정보 수정 전 User 존재 여부 확인 후 수정 진행
    public ResponseEntity<?> updateUser(UserDTO userDTO){
        log.info("updateUser");

        // 수정 전 수정할 User 조회
        if (userRepository.existsById(userDTO.getUid())){
            // User가 존재하는 경우만 수정 진행
            userRepository.save(userDTO.toEntity());

            // 수정 후 수정 데이터 반환
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(userDTO);
        }else{
            // User가 존재하지 않는 경우 NOT_FOUND 응답데이터와 user not found 메세지
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("user not found");
        }
    }

    // User 정보 삭제
    // User 정보 삭제 전 User 존재 여부 확인 후 삭제 진행
    public ResponseEntity<?> deleteUser(String uid){
        log.info("deleteUser");

        // 삭제 전 삭제할 User 조회
        Optional<User> user = userRepository.findById(uid);

        if (user.isPresent()){
            // User가 존재하는 경우 삭제 후 삭제한 User 정보 ResponseEntity로 반환
            userRepository.deleteById(uid);
            return ResponseEntity
                    .ok()
                    .body(user.get());
        }else{
            // User가 존재하지 않는 경우 NOT_FOUND 응답데이터와 user not found 메세지
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("user not found");
        }
    }
}
