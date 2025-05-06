package com.moonjin.realworld.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moonjin.realworld.domain.user.User;
import com.moonjin.realworld.dto.request.user.PutUser;
import com.moonjin.realworld.dto.request.user.Signin;
import com.moonjin.realworld.dto.request.user.Signup;
import com.moonjin.realworld.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    private static final String SESSION_USER_KEY = "user";

    @AfterEach
    void clean() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입")
    void signUpTest1() throws Exception {
        // given
        Signup request = Signup.builder()
                .email("test@gmail.com")
                .password("password")
                .username("Tester")
                .build();

        String json = objectMapper.writeValueAsString(request);

        // when
        mockMvc.perform(post("/api/users")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());

        // then
        assertEquals(1L, userRepository.count());

        User user = userRepository.findAll().get(0);
        assertEquals("test@gmail.com", user.getEmail());
        assertEquals("password", user.getPassword());
        assertEquals("Tester", user.getUsername());
    }

    @Test
    @DisplayName("회원가입 시 이메일은 필수이다.")
    void signUpTest2() throws Exception {
        // given
        Signup request = Signup.builder()
                .password("password")
                .username("Tester")
                .build();

        String json = objectMapper.writeValueAsString(request);

        // when & then
        mockMvc.perform(post("/api/users")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.email").value("이메일을 입력해주세요."))
                .andDo(print());
    }

    @Test
    @DisplayName("로그인")
    void signipTest1() throws Exception {
        // given
        User user = User.builder()
                .username("tester")
                .email("test@gmail.com")
                .password("password")
                .build();
        userRepository.save(user);

        Signin request = Signin.builder()
                .email("test@gmail.com")
                .password("password")
                .build();

        String json = objectMapper.writeValueAsString(request);

        // when
        mockMvc.perform(post("/api/users/login")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.email").value("test@gmail.com"))
                .andExpect(jsonPath("$.user.username").value("tester"))
                .andDo(print());

        // then
    }

    @Test
    @DisplayName("로그인 시 이메일은 필수이다.")
    void signipTest2() throws Exception {
        // given
        Signin request = Signin.builder()
                .password("password")
                .build();

        String json = objectMapper.writeValueAsString(request);

        // when & then
        mockMvc.perform(post("/api/users/login")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.email").value("이메일을 입력해주세요."))
                .andDo(print());
    }

    @Test
    @DisplayName("내 정보 조회")
    void meTest() throws Exception {
        // 1. 세션에 미리 User 객체를 저장
        User sessionUser = User.builder()
                .username("tester")
                .email("test@gmail.com")
                .password("password")
                .build();
        userRepository.save(sessionUser);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SESSION_USER_KEY, sessionUser.getId());

        // 2. GET /users 요청, 세션 전달
        mockMvc.perform(
                        get("/api/users")
                                .session(session)
                                .accept(MediaType.APPLICATION_JSON)
                )
                // 3. 응답 상태·payload 검증
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.email").value(sessionUser.getEmail()))
                .andExpect(jsonPath("$.user.username").value(sessionUser.getUsername()));
    }

    @Test
    @DisplayName("내 정보 수정")
    void putTest() throws Exception {
        // 1. 세션에 미리 User 객체를 저장
        User sessionUser = User.builder()
                .username("tester")
                .email("test@gmail.com")
                .password("password")
                .build();
        userRepository.save(sessionUser);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SESSION_USER_KEY, sessionUser.getId());

        PutUser request = PutUser.builder()
                .email("test@gmail.com")
                .username("tester")
                .password("password")
                .bio("I like to skateboard")
                .image("https://i.stack.imgur.com/xHWG8.jpg")
                .build();

        String json = objectMapper.writeValueAsString(request);

        // 2. GET /users 요청, 세션 전달
        mockMvc.perform(
                        put("/api/users")
                                .session(session)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(APPLICATION_JSON)
                                .content(json)

                )
                // 3. 응답 상태·payload 검증
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.email").value(sessionUser.getEmail()))
                .andExpect(jsonPath("$.user.bio").value("I like to skateboard"))
                .andExpect(jsonPath("$.user.image").value("https://i.stack.imgur.com/xHWG8.jpg"))
                .andExpect(jsonPath("$.user.username").value(sessionUser.getUsername()));

        //then
        assertEquals(1L, userRepository.count());

        User user = userRepository.findAll().get(0);
        assertEquals("test@gmail.com", user.getEmail());
        assertEquals("password", user.getPassword());
        assertEquals("tester", user.getUsername());
        assertEquals("I like to skateboard", user.getBio());
        assertEquals("https://i.stack.imgur.com/xHWG8.jpg", user.getImage());
    }

    @Test
    @DisplayName("특정 유저를 조회한다")
    void getProfileTest() throws Exception {
        // 1. 세션에 미리 User 객체를 저장
        User sessionUser = User.builder()
                .username("tester")
                .email("test@gmail.com")
                .password("password")
                .bio("I like to skateboard")
                .image("https://i.stack.imgur.com/xHWG8.jpg")
                .build();
        userRepository.save(sessionUser);


        // 2. GET /users 요청, 세션 전달
        mockMvc.perform(
                        get("/api/profiles/tester")

                )
                // 3. 응답 상태·payload 검증
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bio").value("I like to skateboard"))
                .andExpect(jsonPath("$.image").value("https://i.stack.imgur.com/xHWG8.jpg"))
                .andExpect(jsonPath("$.username").value(sessionUser.getUsername()));

        //then
    }

    @Test
    @DisplayName("존재하지 않는 유저는 조회할 수 없다.")
    void getProfileTest2() throws Exception {
        // given
        // when //then
        mockMvc.perform(
                        get("/api/profiles/tester")

                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("404"))
                .andExpect(jsonPath("$.message").value("존재하지 않은 유저입니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("특정 유저를 팔로우한다")
    void followTest1() throws Exception {
        // 1. 세션에 미리 User 객체를 저장
        User sessionUser = User.builder()
                .username("tester")
                .email("test@gmail.com")
                .password("password")
                .bio("I like to skateboard")
                .image("https://i.stack.imgur.com/xHWG8.jpg")
                .build();
        userRepository.save(sessionUser);

        User targetUser = User.builder()
                .username("testTarget")
                .email("testTarget@gmail.com")
                .password("password")
                .bio("I like to skateboard")
                .image("https://i.stack.imgur.com/xHWG8.jpg")
                .build();
        userRepository.save(targetUser);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SESSION_USER_KEY, sessionUser.getId());


        // when
        mockMvc.perform(
                        post("/api/profiles/testTarget/follow")
                                .session(session)
                                .accept(MediaType.APPLICATION_JSON)

                )
                // 3. 응답 상태·payload 검증
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bio").value("I like to skateboard"))
                .andExpect(jsonPath("$.image").value("https://i.stack.imgur.com/xHWG8.jpg"))
                .andExpect(jsonPath("$.username").value("testTarget"))
                .andExpect(jsonPath("$.following").value("true"));

        //then
    }

    @Test
    @DisplayName("존재하지 않는 유저는 팔로우 할 수 없다.")
    void followTest2() throws Exception {
        // given
        // 1. 세션에 미리 User 객체를 저장
        User sessionUser = User.builder()
                .username("tester")
                .email("test@gmail.com")
                .password("password")
                .bio("I like to skateboard")
                .image("https://i.stack.imgur.com/xHWG8.jpg")
                .build();
        userRepository.save(sessionUser);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SESSION_USER_KEY, sessionUser.getId());

        // when //then
        mockMvc.perform(
                        post("/api/profiles/testTarget/follow")
                                .session(session)
                                .accept(MediaType.APPLICATION_JSON)

                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("404"))
                .andExpect(jsonPath("$.message").value("존재하지 않은 유저입니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("팔로우한 유저를 언팔로우한다")
    void unFollowTest1() throws Exception {
        // 1. 세션에 미리 User 객체를 저장
        User targetUser = User.builder()
                .username("testTarget")
                .email("testTarget@gmail.com")
                .password("password")
                .bio("I like to skateboard")
                .image("https://i.stack.imgur.com/xHWG8.jpg")
                .build();
        userRepository.save(targetUser);

        User sessionUser = User.builder()
                .username("tester")
                .email("test@gmail.com")
                .password("password")
                .bio("I like to skateboard")
                .image("https://i.stack.imgur.com/xHWG8.jpg")
                .build();
        sessionUser.follow(targetUser);
        userRepository.save(sessionUser);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SESSION_USER_KEY, sessionUser.getId());


        // when
        mockMvc.perform(
                        delete("/api/profiles/testTarget/unfollow")
                                .session(session)
                                .accept(MediaType.APPLICATION_JSON)

                )
                // 3. 응답 상태·payload 검증
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bio").value("I like to skateboard"))
                .andExpect(jsonPath("$.image").value("https://i.stack.imgur.com/xHWG8.jpg"))
                .andExpect(jsonPath("$.username").value("testTarget"))
                .andExpect(jsonPath("$.following").value("false"));

        //then
    }

    @Test
    @DisplayName("존재하지 않는 유저는 언팔로우 할 수 없다.")
    void unFollowTest2() throws Exception {
        // given
        // 1. 세션에 미리 User 객체를 저장
        User sessionUser = User.builder()
                .username("tester")
                .email("test@gmail.com")
                .password("password")
                .bio("I like to skateboard")
                .image("https://i.stack.imgur.com/xHWG8.jpg")
                .build();
        userRepository.save(sessionUser);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SESSION_USER_KEY, sessionUser.getId());

        // when //then
        mockMvc.perform(
                        delete("/api/profiles/testTarget/unfollow")
                                .session(session)
                                .accept(MediaType.APPLICATION_JSON)

                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("404"))
                .andExpect(jsonPath("$.message").value("존재하지 않은 유저입니다."))
                .andDo(print());
    }
}
