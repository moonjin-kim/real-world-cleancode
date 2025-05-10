package com.moonjin.realworld.repository;

import com.moonjin.realworld.domain.user.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    boolean existsByFromUserIdAndToUserId(Long fromUserId, Long toUserId);
    @Query("""
      SELECT f.toUser.id
      FROM Follow f
      WHERE f.fromUser.id = :fromUserId
        AND f.toUser.id   IN :authorIds
    """)
    List<Long> findFollowedUserIds(
            @Param("fromUserId") Long   fromUserId,
            @Param("authorIds") Collection<Long> authorIds
    );
}
