package com.moonjin.realworld.domain.user;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user")
    private User fromUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user")
    private User toUser;

    public Follow(User fromUser, User toUser) {
        this.fromUser = fromUser;
        this.toUser = toUser;
    }

    public Follow() {

    }

    static Follow create(User fromUser, User toUser) {
        return new Follow(fromUser, toUser);
    }

    public User getFollowing() {
        return toUser;
    }
}
