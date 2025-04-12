package com.moonjin.realworld.user.controller;

import com.moonjin.realworld.common.annotation.AuthRequired;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    @GetMapping("/test")
    public String test(@RequestAttribute("userName") String userName) {
        log.info(userName);
        return "test";
    }

    @AuthRequired
    @GetMapping("/hello")
    public String hello() {
        return "Hello authenticated user!";
    }

    @GetMapping("/open")
    public String open() {
        return "Anyone can see this";
    }
}
