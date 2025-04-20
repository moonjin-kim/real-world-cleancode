package com.moonjin.realworld.user.domain;

import com.moonjin.realworld.user.dto.request.PutUser;
import com.moonjin.realworld.user.dto.request.Signup;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String email;

    @Column(length = 50,nullable = false)
    private String password;

    @Column(length = 10,nullable = false)
    private String username;

    @Column(nullable = true)
    private String bio;

    @Column(nullable = true)
    private String image;

    @OneToMany(mappedBy = "fromUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Follow> followings = new HashSet<>();

//    @OneToMany(mappedBy = "toUser", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<Follow> followers = new HashSet<>();

    @Builder
    public User(String email, String password, String username, String bio, String image) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.bio = bio;
        this.image = image;
    }

    public static User of(Signup signup) {
        return User.builder()
                .email(signup.getEmail())
                .password(signup.getPassword())
                .username(signup.getUsername())
                .build();
    }

    public void patch(PutUser request) {
        Optional.ofNullable(request.getEmail())
                .ifPresent(this::putEmail);

        Optional.ofNullable(request.getPassword())
                .ifPresent(this::putPassword);

        Optional.ofNullable(request.getBio())
                .ifPresent(this::putBio);

        Optional.ofNullable(request.getImage())
                .ifPresent(this::putImage);

        Optional.ofNullable(request.getUsername())
                .ifPresent(this::putUsername);
    }

    public void putEmail(String email) {
        this.email = email;
    }

    public void putPassword(String password) {
        this.password = password;
    }

    public void putUsername(String username) {
        this.username = username;
    }

    public void putBio(String bio) {
        this.bio = bio;
    }

    public void putImage(String image) {
        this.image = image;
    }

    public boolean authNotPass(String password) {
        return !this.password.equals(password);
    }

    public void follow(User target) {
        validateNotSelf(target);
        validateNotAlreadyFollowing(target);
        followings.add(Follow.create(this, target));
    }

    private void validateNotAlreadyFollowing(User target) {
        boolean alreadyFollowed = followings.stream()
                .anyMatch(f -> f.getFollowing().equals(target));
        if (alreadyFollowed) {
            throw new IllegalStateException("이미 팔로우한 사용자입니다.");
        }
    }

    public void unfollow(User target) {
        Follow follow = followings.stream()
                .filter(f -> f.getFollowing().equals(target))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("팔로우하고 있지 않은 사용자입니다."));
        followings.remove(follow);
    }

    public boolean isFollowing(User target) {
        return followings.stream()
                .anyMatch(f -> f.getFollowing().equals(target));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        if (this.id == null || user.id == null) return false;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    private void validateNotSelf(User target) {
        if (this.equals(target)) {
            throw new IllegalArgumentException("자기 자신은 팔로우할 수 없습니다.");
        }
    }
}
