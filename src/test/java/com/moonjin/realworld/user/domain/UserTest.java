package com.moonjin.realworld.user.domain;

import com.moonjin.realworld.user.dto.request.Signup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class UserTest {
    @Test
    @DisplayName("회원가입 성공")
    void signUpTest1() {
        // given
        Signup signup = Signup.builder()
                .email("realword@gmail.com")
                .password("realworld123!")
                .username("RealWorld")
                .build();

        // when
        userService.signup(signup);

        // then
        assertEquals(1, userRepository.count());

        User user = userRepository.findAll().iterator().next();
        assertEquals("realword@gmail.com", user.getEmail());
        assertEquals("realworld123!", user.getPassword());
        assertEquals("RealWorld", user.getUsername());
    }
}
