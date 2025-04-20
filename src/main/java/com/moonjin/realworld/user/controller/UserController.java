package com.moonjin.realworld.user.controller;

import com.moonjin.realworld.common.annotation.AuthRequired;
import com.moonjin.realworld.user.domain.User;
import com.moonjin.realworld.user.dto.request.PutUser;
import com.moonjin.realworld.user.dto.request.Signin;
import com.moonjin.realworld.user.dto.request.Signup;
import com.moonjin.realworld.user.dto.response.Profile;
import com.moonjin.realworld.user.dto.response.UserDetail;
import com.moonjin.realworld.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserService userService;
    private static final String SESSION_USER_KEY = "user";

    @PostMapping("/users")
    public UserDetail signUp(HttpSession httpSession, @RequestBody @Valid Signup request) {
        User user = userService.signup(request);
        httpSession.setAttribute(SESSION_USER_KEY, user.getId());
        return UserDetail.of(user);
    }

    @GetMapping("/users")
    @AuthRequired
    public UserDetail me(HttpSession httpSession) {
        Long user = (Long) httpSession.getAttribute(SESSION_USER_KEY);
        return userService.getUserDetail(user);
    }

    @PostMapping("/users/login")
    public UserDetail login(HttpSession httpSession,@RequestBody @Valid Signin request) {
        User user = userService.signIn(request);
        httpSession.setAttribute(SESSION_USER_KEY, user.getId());
        return UserDetail.of(user);
    }

    @PutMapping("/users")
    @AuthRequired
    public UserDetail put(HttpSession httpSession, @RequestBody @Valid PutUser request) {
        Long user = (Long) httpSession.getAttribute(SESSION_USER_KEY);
        return userService.put(user, request);
    }

    @GetMapping("/profiles/{username}")
    public Profile getProfiles(HttpSession httpSession, @PathVariable String username) {
        Long user = (Long) httpSession.getAttribute(SESSION_USER_KEY);
        return userService.getProfileFrom(username, user);
    }

    @PostMapping("/profiles/{username}/follow")
    public Profile follow(HttpSession httpSession, @PathVariable String username) {
        Long user = (Long) httpSession.getAttribute(SESSION_USER_KEY);
        return userService.follow(user, username);
    }


    @DeleteMapping("/profiles/{username}/unfollow")
    public Profile unfollow(HttpSession httpSession, @PathVariable String username) {
        Long user = (Long) httpSession.getAttribute(SESSION_USER_KEY);
        return userService.unFollow(user, username);
    }
}
