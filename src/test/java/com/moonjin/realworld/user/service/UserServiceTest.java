package com.moonjin.realworld.user.service;

import com.moonjin.realworld.common.exception.AlreadyExistsEmailException;
import com.moonjin.realworld.user.domain.User;
import com.moonjin.realworld.user.dto.request.Signup;
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
}
