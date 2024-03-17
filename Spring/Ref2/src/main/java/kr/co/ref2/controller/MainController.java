package kr.co.ref2.controller;

import kr.co.ref2.dto.UserDTO;
import kr.co.ref2.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@Controller
public class MainController {

    private final UserService service;

    @GetMapping("/")
    public String index(){
        return "/index";
    }

    @GetMapping("/user/login")
    public String login(){
        log.info("userLogin - GET");
        return "/user/login";
    }

    @GetMapping("/user/register")
    public String register(){
        log.info("userLogin - GET");
        return "/user/register";
    }

    @PostMapping("/user/register")
    public String register(@RequestBody UserDTO userDTO){
        log.info("register-Post");
        service.insertUser(userDTO);
        return "redirect:/user/login";
    }

    // ROLE_ANONYMOUS 이게 문제인듯
    // 권한 확인 어노테이션
    @PreAuthorize("hasRole('USER') or hasRole('MANAGER') or hasRole('ADMIN')")
    @GetMapping("/user/list")
    public String list(){
        log.info("userList - GET");
        service.logUserAuthorities();// 사용자 권한 확인
        return "/user/list";
    }
}
