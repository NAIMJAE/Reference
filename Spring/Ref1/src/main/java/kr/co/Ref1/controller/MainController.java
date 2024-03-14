package kr.co.Ref1.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@AllArgsConstructor
@Controller
public class MainController {
    // 페이지 호출(페이지 로드)을 담당하는 Controller
    @GetMapping("/index")
    public String index(){
        log.info("index - GET");
        return "/index";
    }
    @GetMapping("/user/list")
    public String list(){
        log.info("user/list - GET");
        return "/user/list";
    }
    @GetMapping("/user/register")
    public String register(){
        log.info("user/register - GET");
        return "/user/register";
    }
    @GetMapping("/user/modify")
    public String modify(){
        log.info("user/modify - GET");
        return "/user/modify";
    }
}
