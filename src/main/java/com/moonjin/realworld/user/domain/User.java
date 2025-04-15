package com.moonjin.realworld.user.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

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

    @ManyToMany
    @JoinTable(name = "user_follows",
            joinColumns = @JoinColumn(name = "follower_id"),
            inverseJoinColumns = @JoinColumn(name = "followee_id"))
    private Set<User> followings = new HashSet<>();


    @Builder
    public User(String email, String password, String username, String bio, String image) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.bio = bio;
        this.image = image;
    }

    public void putInfo(String bio, String image) {
        this.bio = bio;
        this.image = image;
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

    public void follow(User userToFollow) {
        if (this.equals(userToFollow)) {
            throw new IllegalArgumentException("자기 자신은 팔로우할 수 없습니다.");
        }
        if (followings.contains(userToFollow)) {
            throw new IllegalStateException("이미 팔로우한 사용자입니다.");
        }
        followings.add(userToFollow);
    }

    public void unfollow(User userToUnfollow) {
        if (!followings.contains(userToUnfollow)) {
            throw new IllegalStateException("팔로우하고 있지 않은 사용자입니다.");
        }
        followings.remove(userToUnfollow);
    }

}
