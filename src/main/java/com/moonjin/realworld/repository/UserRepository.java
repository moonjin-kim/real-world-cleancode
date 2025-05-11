package com.moonjin.realworld.repository;

import com.moonjin.realworld.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String username);
    Optional<User> findByUsername(String username);
    @Query("select distinct u from User u left join fetch u.followings where u.id = :id")
    Optional<User> findByIdWithFollowings(@Param("id") Long id);
}
