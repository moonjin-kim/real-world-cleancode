package com.moonjin.realworld.user.controller;

import com.moonjin.realworld.common.annotation.AuthRequired;
import com.moonjin.realworld.user.dto.request.SignupRequest;
import com.moonjin.realworld.user.dto.response.AuthResponse;
import com.moonjin.realworld.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

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

    @PostMapping("")
    public AuthResponse signUp(@RequestBody SignupRequest request) {
        AuthResponse response = userService.signup(request);
        log.info(response.toString());
        return response;
    }
}
