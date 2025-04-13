package com.moonjin.realworld.user.controller;

import com.moonjin.realworld.common.annotation.AuthRequired;
import com.moonjin.realworld.user.dto.request.Signup;
import com.moonjin.realworld.user.dto.response.AuthResponse;
import com.moonjin.realworld.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private static final String SESSION_USER_KEY = "user";

    @PostMapping("")
    public AuthResponse signUp(@RequestBody Signup request, HttpSession httpSession) {
        AuthResponse response = userService.signup(request);
        httpSession.setAttribute(SESSION_USER_KEY, response);
        return response;
    }

    @GetMapping
    @AuthRequired
    public AuthResponse me(HttpSession httpSession) {
        return (AuthResponse) httpSession.getAttribute(SESSION_USER_KEY);
    }
}
