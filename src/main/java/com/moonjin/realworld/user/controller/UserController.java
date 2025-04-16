package com.moonjin.realworld.user.controller;

import com.moonjin.realworld.common.annotation.AuthRequired;
import com.moonjin.realworld.user.domain.User;
import com.moonjin.realworld.user.dto.request.PutRequest;
import com.moonjin.realworld.user.dto.request.Signin;
import com.moonjin.realworld.user.dto.request.Signup;
import com.moonjin.realworld.user.dto.response.Profile;
import com.moonjin.realworld.user.dto.response.UserDetail;
import com.moonjin.realworld.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserService userService;
    private static final String SESSION_USER_KEY = "user";

    @PostMapping("/users")
    public UserDetail signUp(@RequestBody Signup request, HttpSession httpSession) {
        User user = userService.signup(request);
        httpSession.setAttribute(SESSION_USER_KEY, user);
        return UserDetail.of(user);
    }

    @GetMapping("/users")
    @AuthRequired
    public UserDetail me(HttpSession httpSession) {
        User user = (User) httpSession.getAttribute(SESSION_USER_KEY);
        return UserDetail.of(user);
    }

    @PostMapping("/users/login")
    public UserDetail login(@RequestBody Signin request, HttpSession httpSession) {
        User user = userService.signin(request);
        httpSession.setAttribute(SESSION_USER_KEY, user);
        return UserDetail.of(user);
    }

    @PutMapping("/users")
    @AuthRequired
    public UserDetail put(@RequestBody PutRequest request, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute(SESSION_USER_KEY);
        return userService.put(user.getId(), request);
    }

    @GetMapping("/profiles/{username}")
    public Profile getProfiles(@PathVariable String username) {
        return userService.getProfileFrom(username);
    }

    @PostMapping("/profiles/{username}/follow")
    public Profile follow(@PathVariable String username, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute(SESSION_USER_KEY);
        return userService.follow(user.getId(), username);
    }


    @DeleteMapping("/profiles/{username}/unfollow")
    public Profile unfollow(@PathVariable String username, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute(SESSION_USER_KEY);
        return userService.unFollow(user.getId(), username);
    }
}
