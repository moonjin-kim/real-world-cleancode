package com.moonjin.realworld.user.controller;

import com.moonjin.realworld.common.annotation.AuthRequired;
import com.moonjin.realworld.user.domain.User;
import com.moonjin.realworld.user.dto.request.Signin;
import com.moonjin.realworld.user.dto.request.Signup;
import com.moonjin.realworld.user.dto.response.UserDetail;
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
    public UserDetail signUp(@RequestBody Signup request, HttpSession httpSession) {
        User user = userService.signup(request);
        httpSession.setAttribute(SESSION_USER_KEY, user);
        return UserDetail.of(user);
    }

    @GetMapping
    @AuthRequired
    public UserDetail me(HttpSession httpSession) {
        return (UserDetail) httpSession.getAttribute(SESSION_USER_KEY);
    }

    @PostMapping("login")
    public UserDetail login(@RequestBody Signin request, HttpSession httpSession) {
        User user = userService.signin(request);
        httpSession.setAttribute(SESSION_USER_KEY, user);
        return UserDetail.of(user);
    }
}
