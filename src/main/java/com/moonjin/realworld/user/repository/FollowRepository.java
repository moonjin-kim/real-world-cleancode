package com.moonjin.realworld.user.repository;

import com.moonjin.realworld.user.domain.Follow;
import com.moonjin.realworld.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository  extends JpaRepository<Follow, Long> {
    boolean existsByFromUserAndToUser(User fromUser, User toUser);
    Optional<Follow> findByFromUserAndToUser(User fromUser, User toUser);
}
