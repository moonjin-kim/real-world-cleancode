package com.moonjin.realworld.user.repository;

import com.moonjin.realworld.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String username);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmailAndPassword(String email, String password);
}
