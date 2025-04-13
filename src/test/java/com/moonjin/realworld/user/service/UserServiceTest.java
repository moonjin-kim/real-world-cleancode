package com.moonjin.realworld.user.service;

import com.moonjin.realworld.common.exception.AlreadyExistsEmailException;
import com.moonjin.realworld.common.exception.Unauthorized;
import com.moonjin.realworld.user.domain.User;
import com.moonjin.realworld.user.dto.request.Signin;
import com.moonjin.realworld.user.dto.request.Signup;
import com.moonjin.realworld.user.dto.response.AuthResponse;
import com.moonjin.realworld.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @AfterEach
    void clean() {
        userRepository.deleteAll();
    }

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

    @Test
    @DisplayName("중복된 이메일이면 회원가입에 실패한다.")
    void signUpTest2() {
        // given
        User user = User.builder()
                .email("realword@gmail.com")
                .password("realworld123!")
                .username("RealWorld")
                .build();
        userRepository.save(user);

        Signup signup = Signup.builder()
                .email("realword@gmail.com")
                .password("realworld123!")
                .username("RealWorld")
                .build();

        // when
        // then
        assertThrows(AlreadyExistsEmailException.class, () -> userService.signup(signup));
    }

    @Test
    @DisplayName("이메일 패스워드로 로그인한다")
    void signinTest1() {
        // given
        User user = User.builder()
                .email("realword@gmail.com")
                .password("realworld123!")
                .username("RealWorld")
                .build();
        userRepository.save(user);

        Signin signin = Signin.builder()
                .email("realword@gmail.com")
                .password("realworld123!")
                .build();

        // when
        AuthResponse result = userService.signin(signin);

        // then
        assertEquals("realword@gmail.com", result.getEmail());
        assertEquals("RealWorld", result.getUsername());
    }

    @Test
    @DisplayName("존재하지 않는 계정으로 로그인 시도 시 에러가 발생한다")
    void signinTest2() {
        // given
        Signin signin = Signin.builder()
                .email("realword@gmail.com")
                .password("realworld123!")
                .build();

        // when

        // then
        assertThrows(Unauthorized.class, () -> userService.signin(signin));
    }

    @Test
    @DisplayName("잘못된 비밀번호 입력시 에러가 발생한다.")
    void signinTest3() {
        // given
        User user = User.builder()
                .email("realword@gmail.com")
                .password("realworld123!")
                .username("RealWorld")
                .build();
        userRepository.save(user);

        Signin signin = Signin.builder()
                .email("realword@gmail.com")
                .password("realworld1234!")
                .build();

        // when

        // then
        assertThrows(Unauthorized.class, () -> userService.signin(signin));
    }
}
