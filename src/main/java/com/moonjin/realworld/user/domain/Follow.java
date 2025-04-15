package com.moonjin.realworld.user.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "follows", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"from_user_id", "to_user_id"})
})
public class Follow {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 팔로우 하는 사람
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user_id")
    private User fromUser;

    // 팔로우 당하는 사람
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user_id")
    private User toUser;

    @Builder
    public Follow(User fromUser, User toUser) {
        this.fromUser = fromUser;
        this.toUser = toUser;
    }
}
