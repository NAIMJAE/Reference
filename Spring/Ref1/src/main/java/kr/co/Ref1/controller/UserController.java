package kr.co.Ref1.controller;

import kr.co.Ref1.dto.UserDTO;
import kr.co.Ref1.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/*
    @RequestBody
    - 요청 본문 내용에 포함된 데이터를 Java객체로 변환
    - 클라이언트에서 전송되는 JSOn 데이터를 수신

    @ResponseBody
    - 작업의 결과를 foward하거나 새로고침 하지 않고 데이터를 클라이언트에게 전달
    - 메서드의 변환값을 응답객체 내용 본문으로 출력
    - JSON 출력 어노테이션

    @PathVariable
    - 주소 파라미터 값을 수신
    - 컨트롤러 메서드의 파라미터를 URI의 일부로 전달되는 경로 변수에 바인딩할 때 사용
    - 주로 RESTful API에서 자원의 식별자를 전달할 때 사용

    @RestController
    - API 요청을 처리하는 메서드의 결과를 응답 객체 본문에 출력시키는 클래스
    - 해당 클래스가 RESTful 웹 서비스의 엔드포인트를 처리하는 컨트롤러임을 나타냄
    - 어노테이션이 지정된 클래스의 각 메서드는 HTTP 요청을 처리하고, 클라이언트에게 JSON, XML 등의 형식으로 응답을 반환

    @ResponseEntity
    - API를 요청한 사용자에게 응답데이터를 구성해 클라이언트에게 부가정보 제공하기 위한 클래스
    - 일반적으로 상태코드(header), 본문 내용(body)을 구성해서 제공
*/

/*
    ResponseEntity
    - 일반적인 Controller를 이용한 HTTP 통신에서는 클라이언트가 요청한 작업을 수행한 뒤 HTTP페이지를 forward하는 방식을 사용
    - 이 방식을 사용하면 페이지를 새로 로드해야 함
    - 페이지가 로드 될때 처리해야하는 작업같은 경우에는 일반적인 통신 방식 사용

    - 그런데 새로운 페이지 로드가 발생하면 안되는, 사용자의 동작에 따라서 진행되는 동적인 작업 요청 같은 경우 ResponseEntity 사용
    - 보통 이러한 요청을 처리하기 위해 자바스크립트의 fetch를 사용하며, fetch의 response값은 JSON 데이터 형식
    - 그래서 Controller에서 사용자의 요청을 처리한 뒤 HTTP통신이 아닌 JSON과 같은 데이터 형식을 리턴하기 위해 ResponseEntity 사용
    - ResponseEntity은 응답 상태 코드, 데이터, 해당 작업의 결과들을 설정해 전송할 수 있음
 */

@Slf4j
@AllArgsConstructor
@RestController
public class UserController {
    private final UserService userService;
    
    // User 목록
    @GetMapping("/user")
    public ResponseEntity<List<UserDTO>> list(){
        log.info("userList - GET");
        return userService.selectUsers();
    }

    // User 정보 조회
    @GetMapping("/user/{uid}")
    public ResponseEntity<?> user(@PathVariable("uid") String uid){
        log.info("userModify - GET");
        return userService.selectUser(uid);
    }

    // User 등록
    @PostMapping("/user")
    public ResponseEntity<?> register(@RequestBody @Validated UserDTO userDTO){
        log.info("userRegister - GET");
        return userService.insertUser(userDTO);
    }

    // User 수정
    @PutMapping("/user")
    public ResponseEntity<?> modify(@RequestBody UserDTO userDTO){
        log.info("userModify - GET");
        return userService.updateUser(userDTO);
    }

    // User 삭제
    @DeleteMapping("/user/{uid}")
    public ResponseEntity<?> delete(@PathVariable("uid") String uid){
        log.info("userDelete - DELETE");
        return userService.deleteUser(uid);
    }
}
