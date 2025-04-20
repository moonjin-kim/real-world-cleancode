package com.moonjin.realworld.user.domain;

import com.moonjin.realworld.user.dto.request.PutUser;
import com.moonjin.realworld.user.dto.request.Signup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class UserTest {
    @Test
    @DisplayName("유저를 생성한다")
    void signUpTest1() {
        // given
        Signup signup = Signup.builder()
                .email("realword@gmail.com")
                .password("realworld123!")
                .username("RealWorld")
                .build();

        // when
        User user = User.of(signup);

        // then
        assertEquals("realword@gmail.com", user.getEmail());
        assertEquals("realworld123!", user.getPassword());
        assertEquals("RealWorld", user.getUsername());
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

        PutUser request = PutUser
                .builder()
                .email("realword@gmail.com")
                .bio("I like to skateboard")
                .image("https://i.stack.imgur.com/xHWG8.jpg")
                .username("RealWorld2")
                .password("realworld1234!")
                .build();

        // when
        user.patch(request);

        // then

        assertEquals("realword@gmail.com", user.getEmail());
        assertEquals("I like to skateboard", user.getBio());
        assertEquals("https://i.stack.imgur.com/xHWG8.jpg", user.getImage());
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

        PutUser request = PutUser
                .builder()
                .bio("I like to skateboard")
                .image("https://i.stack.imgur.com/xHWG8.jpg")
                .username("RealWorld2")
                .password("realworld1234!")
                .build();

        // when
        user.patch(request);

        // then

        assertEquals("realword@gmail.com", user.getEmail());
        assertEquals("I like to skateboard", user.getBio());
        assertEquals("https://i.stack.imgur.com/xHWG8.jpg", user.getImage());
        assertEquals("RealWorld2", user.getUsername());
    }

    @Test
    @DisplayName("비밀번호가 같으면 false 반환한다.")
    void authNotPassTest1() {
        // given
        User user = User.builder()
                .email("realword@gmail.com")
                .password("realworld123!")
                .username("RealWorld")
                .build();

        // when
        boolean result = user.authNotPass("realworld123!");

        // then
        assertFalse(result);
    }

    @Test
    @DisplayName("비밀번호가 다르면 true를 반환한다.")
    void authNotPassTest2() {
        // given
        User user = User.builder()
                .email("realword@gmail.com")
                .password("realworld123!")
                .username("RealWorld")
                .build();

        // when
        boolean result = user.authNotPass("realworld123");

        // then
        assertTrue(result);
    }

    @Test
    @DisplayName("다른 유저를 팔로우한다.")
    void follow() {
        // given
        User user = User.builder()
                .email("realword@gmail.com")
                .password("realworld123!")
                .username("RealWorld")
                .build();

        User target = User.builder()
                .email("target@gmail.com")
                .password("target123!")
                .username("Target")
                .build();

        // when
        user.follow(target);

        // then
        assertEquals(1, user.getFollowings().size());
    }

    @Test
    @DisplayName("자기 자신을 팔로우하면 에러가 발생한다")
    void followMeTest() {
        // given
        User user = User.builder()
                .email("realword@gmail.com")
                .password("realworld123!")
                .username("RealWorld")
                .build();

        // when
        // then
        assertThrows(IllegalArgumentException.class, () -> user.follow(user));
    }

    @Test
    @DisplayName("이미 팔로우한 유저를 팔로우하면 에러가 발생한다")
    void followIsAlreadyFollowedTest() {
        // given
        User user = User.builder()
                .email("realword@gmail.com")
                .password("realworld123!")
                .username("RealWorld")
                .build();

        User target = User.builder()
                .email("target@gmail.com")
                .password("target123!")
                .username("Target")
                .build();

        // when
        user.follow(target);
        // then
        assertThrows(IllegalStateException.class, () -> user.follow(target));
    }

    @Test
    @DisplayName("팔로우 된 유저를 언팔로우 한다.")
    void unfollowTest() {
        // given
        User user = User.builder()
                .email("realword@gmail.com")
                .password("realworld123!")
                .username("RealWorld")
                .build();

        User target = User.builder()
                .email("target@gmail.com")
                .password("target123!")
                .username("Target")
                .build();
        user.follow(target);

        // when
        user.unfollow(target);

        // then
        assertEquals(0, user.getFollowings().size());
    }

    @Test
    @DisplayName("자기 자신을 언팔로우 할 수 없다.")
    void unfollowMeTest() {
        // given
        User user = User.builder()
                .email("realword@gmail.com")
                .password("realworld123!")
                .username("RealWorld")
                .build();

        // when
        // then
        assertThrows(IllegalStateException.class, () -> user.unfollow(user));
    }

    @Test
    @DisplayName("팔로우 하지 않은 유저를 언팔로우 할 수 없다.")
    void unfollowNotFollowedTest() {
        // given
        User user = User.builder()
                .email("realword@gmail.com")
                .password("realworld123!")
                .username("RealWorld")
                .build();

        User target = User.builder()
                .email("target@gmail.com")
                .password("target123!")
                .username("Target")
                .build();

        // when
        // then
        assertThrows(IllegalStateException.class, () -> user.unfollow(target));
    }

    @Test
    @DisplayName("팔로우 여부를 확인한다.")
    void isFollowingTest() {
        // given
        User user = User.builder()
                .email("realword@gmail.com")
                .password("realworld123!")
                .username("RealWorld")
                .build();

        User target = User.builder()
                .email("target@gmail.com")
                .password("target123!")
                .username("Target")
                .build();
        user.follow(target);

        // when
        boolean result = user.isFollowing(target);

        // then
        assertTrue(result);
    }

    @Test
    @DisplayName("팔로우 되지 않았으면 false를 반환한다.")
    void isFollowingTest2() {
        // given
        User user = User.builder()
                .email("realword@gmail.com")
                .password("realworld123!")
                .username("RealWorld")
                .build();

        User target = User.builder()
                .email("target@gmail.com")
                .password("target123!")
                .username("Target")
                .build();

        // when
        boolean result = user.isFollowing(target);

        // then
        assertFalse(result);
    }
}
