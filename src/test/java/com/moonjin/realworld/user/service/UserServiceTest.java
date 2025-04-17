package com.moonjin.realworld.user.service;

import com.moonjin.realworld.common.exception.AlreadyExistsEmailException;
import com.moonjin.realworld.common.exception.Unauthorized;
import com.moonjin.realworld.common.exception.UserNotFoundException;
import com.moonjin.realworld.user.domain.User;
import com.moonjin.realworld.user.dto.request.PutRequest;
import com.moonjin.realworld.user.dto.request.Signin;
import com.moonjin.realworld.user.dto.request.Signup;
import com.moonjin.realworld.user.dto.response.Profile;
import com.moonjin.realworld.user.dto.response.UserDetail;
import com.moonjin.realworld.user.repository.UserRepository;
import jakarta.transaction.Transactional;
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
        UserDetail result = userService.put(user.getId(), putRequest);

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
        UserDetail result = userService.put(user.getId(), putRequest);

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

        PutRequest putRequest = PutRequest
                .builder()
                .bio("I like to skateboard")
                .image("https://i.stack.imgur.com/xHWG8.jpg")
                .username("RealWorld2")
                .password("realworld1234!")
                .build();

        // when
        // then
        assertThrows(Unauthorized.class, () -> userService.put(2L, putRequest));
    }

    @Test
    @DisplayName("유저 이름으로 프로필을 조회할 수 있다.")
    void getProfileTest1() {
        // given
        User user = User.builder()
                .email("realword@gmail.com")
                .password("realworld123!")
                .username("RealWorld")
                .bio("I like to skateboard")
                .image("https://i.stack.imgur.com/xHWG8.jpg")
                .build();
        userRepository.save(user);

        // when
        Profile result = userService.getProfileFrom("RealWorld", user.getId());
        // then
        assertEquals("I like to skateboard", result.getBio());
        assertEquals("https://i.stack.imgur.com/xHWG8.jpg", result.getImage());
        assertEquals("RealWorld", result.getUsername());
    }

    @Test
    @DisplayName("존재하지 않는 유저의 프로필을 조회시 에러가 발생한다.")
    void getProfileTest2() {
        // given

        // when
        // then
        assertThrows(UserNotFoundException.class, () -> userService.getProfileFrom("RealWorld", 1L));
    }

    @Test
    @DisplayName("유저 이름으로 팔로우 할 수 있다.")
    void followTest1() {
        // given
        User follower = User.builder()
                .email("realword1@gmail.com")
                .password("realworld123!")
                .username("RealWorld1")
                .bio("I like to skateboard")
                .image("https://i.stack.imgur.com/xHWG8.jpg")
                .build();
        userRepository.save(follower);

        User followee = User.builder()
                .email("realword2@gmail.com")
                .password("realworld123!")
                .username("RealWorld2")
                .bio("I like to skateboard")
                .image("https://i.stack.imgur.com/xHWG8.jpg")
                .build();
        userRepository.save(followee);

        // when
        Profile result = userService.follow(follower.getId(), "RealWorld2");
        // then
        assertEquals("I like to skateboard", result.getBio());
        assertEquals("https://i.stack.imgur.com/xHWG8.jpg", result.getImage());
        assertEquals("RealWorld2", result.getUsername());
        assertEquals(true, result.getFollowing());

        User updatedFollower = userRepository.findByIdWithFollowings(follower.getId()).orElseThrow();
        assertTrue(updatedFollower.getFollowings().stream()
                .anyMatch(u -> u.getToUser().equals(followee)));
    }

    @Test
    @DisplayName("존재하지 않는 유저를 팔로우 할 수 없다.")
    void followTest2() {
        // given
        User user1 = User.builder()
                .email("realword1@gmail.com")
                .password("realworld123!")
                .username("RealWorld1")
                .bio("I like to skateboard")
                .image("https://i.stack.imgur.com/xHWG8.jpg")
                .build();
        userRepository.save(user1);

        // when
        // then
        assertThrows(UserNotFoundException.class, () -> userService.follow(user1.getId(), "RealWorld2"));
    }

    @Test
    @DisplayName("이미 팔로우한 유저를 팔로우 할 수 없다.")
    void followTest3() {
        // given
        User user1 = User.builder()
                .email("realword1@gmail.com")
                .password("realworld123!")
                .username("RealWorld1")
                .bio("I like to skateboard")
                .image("https://i.stack.imgur.com/xHWG8.jpg")
                .build();
        userRepository.save(user1);

        User user2 = User.builder()
                .email("realword2@gmail.com")
                .password("realworld123!")
                .username("RealWorld2")
                .bio("I like to skateboard")
                .image("https://i.stack.imgur.com/xHWG8.jpg")
                .build();
        userRepository.save(user2);

        // when
        userService.follow(user1.getId(), "RealWorld2");
        // then
        assertThrows(IllegalStateException.class, () -> userService.follow(user1.getId(), "RealWorld2"));
    }

    @Test
    @DisplayName("자기 자신을 팔로우 할 수 없다.")
    void followTest4() {
        // given
        User user1 = User.builder()
                .email("realword1@gmail.com")
                .password("realworld123!")
                .username("RealWorld1")
                .bio("I like to skateboard")
                .image("https://i.stack.imgur.com/xHWG8.jpg")
                .build();
        userRepository.save(user1);

        // when
        // then
        assertThrows(IllegalArgumentException.class, () -> userService.follow(user1.getId(), "RealWorld1"));
    }

    @Test
    @DisplayName("유저 이름으로 팔로우한 유저를 언팔로우 할 수 있다.")
    void unfollowTest1() {
        // given
        User follower = User.builder()
                .email("realword1@gmail.com")
                .password("realworld123!")
                .username("RealWorld1")
                .bio("I like to skateboard")
                .image("https://i.stack.imgur.com/xHWG8.jpg")
                .build();
        userRepository.save(follower);

        User followee = User.builder()
                .email("realword2@gmail.com")
                .password("realworld123!")
                .username("RealWorld2")
                .bio("I like to skateboard")
                .image("https://i.stack.imgur.com/xHWG8.jpg")
                .build();
        userRepository.save(followee);
        userService.follow(follower.getId(), "RealWorld2");

        // when
        Profile result = userService.unFollow(follower.getId(), "RealWorld2");
        // then
        assertEquals("I like to skateboard", result.getBio());
        assertEquals("https://i.stack.imgur.com/xHWG8.jpg", result.getImage());
        assertEquals("RealWorld2", result.getUsername());
        assertEquals(false, result.getFollowing());

        User updatedFollower = userRepository.findByIdWithFollowings(follower.getId()).orElseThrow();
        assertFalse(updatedFollower.getFollowings().stream()
                .anyMatch(u -> u.getToUser().equals(followee)));
    }

    @Test
    @DisplayName("존재하지 않는 유저를 언팔로우 할 수 없다.")
    void unFollowTest2() {
        // given
        User user1 = User.builder()
                .email("realword1@gmail.com")
                .password("realworld123!")
                .username("RealWorld1")
                .bio("I like to skateboard")
                .image("https://i.stack.imgur.com/xHWG8.jpg")
                .build();
        userRepository.save(user1);

        // when
        // then
        assertThrows(UserNotFoundException.class, () -> userService.follow(user1.getId(), "RealWorld2"));
    }

    @Test
    @DisplayName("팔로우하지 않은 유저를 언팔로우 할 수 없다.")
    void unFollowTest3() {
        // given
        User user1 = User.builder()
                .email("realword1@gmail.com")
                .password("realworld123!")
                .username("RealWorld1")
                .bio("I like to skateboard")
                .image("https://i.stack.imgur.com/xHWG8.jpg")
                .build();
        userRepository.save(user1);

        User user2 = User.builder()
                .email("realword2@gmail.com")
                .password("realworld123!")
                .username("RealWorld2")
                .bio("I like to skateboard")
                .image("https://i.stack.imgur.com/xHWG8.jpg")
                .build();
        userRepository.save(user2);

        // when
        // then
        assertThrows(IllegalArgumentException.class, () -> userService.unFollow(user1.getId(), "RealWorld2"));
    }
}
