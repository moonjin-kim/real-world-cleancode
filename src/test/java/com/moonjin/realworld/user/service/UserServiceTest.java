package com.moonjin.realworld.user.service;

import com.moonjin.realworld.common.exception.AlreadyExistsEmailException;
import com.moonjin.realworld.common.exception.Unauthorized;
import com.moonjin.realworld.user.domain.User;
import com.moonjin.realworld.user.dto.request.PutRequest;
import com.moonjin.realworld.user.dto.request.Signin;
import com.moonjin.realworld.user.dto.request.Signup;
import com.moonjin.realworld.user.dto.response.UserDetail;
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
        User result = userService.signin(signin);

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

    @Test
    @DisplayName("유저의 정보를 수정한다.")
    void putTest1() {
        // given
        User user = User.builder()
                .email("realword@gmail.com")
                .password("realworld123!")
                .username("RealWorld")
                .build();
        userRepository.save(user);

        PutRequest putRequest = PutRequest
                .builder()
                .email("realword@gmail.com")
                .bio("I like to skateboard")
                .image("https://i.stack.imgur.com/xHWG8.jpg")
                .username("RealWorld2")
                .password("realworld1234!")
                .build();

        // when
        UserDetail result = userService.put(user, putRequest);

        // then

        assertEquals("realword@gmail.com", result.getEmail());
        assertEquals("I like to skateboard", result.getBio());
        assertEquals("https://i.stack.imgur.com/xHWG8.jpg", result.getImage());

        User user2 = userRepository.findAll().iterator().next();
        assertEquals("realword@gmail.com", user2.getEmail());
        assertEquals("I like to skateboard", user2.getBio());
        assertEquals("https://i.stack.imgur.com/xHWG8.jpg", user2.getImage());
    }

    @Test
    @DisplayName("유저 정보를 수정할 때 request에 포함된 값만 수정된다.")
    void putTest2() {
        // given
        User user = User.builder()
                .email("realword@gmail.com")
                .password("realworld123!")
                .username("RealWorld")
                .build();
        userRepository.save(user);

        PutRequest putRequest = PutRequest
                .builder()
                .bio("I like to skateboard")
                .image("https://i.stack.imgur.com/xHWG8.jpg")
                .username("RealWorld2")
                .password("realworld1234!")
                .build();

        // when
        UserDetail result = userService.put(user, putRequest);

        // then

        assertEquals("realword@gmail.com", result.getEmail());
        assertEquals("I like to skateboard", result.getBio());
        assertEquals("https://i.stack.imgur.com/xHWG8.jpg", result.getImage());
        assertEquals("RealWorld2", result.getUsername());

        User user2 = userRepository.findAll().iterator().next();
        assertEquals("realword@gmail.com", user2.getEmail());
        assertEquals("I like to skateboard", user2.getBio());
        assertEquals("https://i.stack.imgur.com/xHWG8.jpg", user2.getImage());
        assertEquals("realworld1234!", user2.getPassword());
        assertEquals("RealWorld2", user2.getUsername());
    }

    @Test
    @DisplayName("유저가 존재하지 않으면 정보를 수정할 수 없다.")
    void putTest3() {
        // given
        User user = User.builder()
                .email("realword@gmail.com")
                .password("realworld123!")
                .username("RealWorld")
                .build();
        userRepository.save(user);

        User user2 = User.builder()
                .email("realword2@gmail.com")
                .password("realworld1234!")
                .username("RealWorld2")
                .build();

        PutRequest putRequest = PutRequest
                .builder()
                .bio("I like to skateboard")
                .image("https://i.stack.imgur.com/xHWG8.jpg")
                .username("RealWorld2")
                .password("realworld1234!")
                .build();

        // when
        // then
        assertThrows(Unauthorized.class, () -> userService.put(user2, putRequest));
    }
}
