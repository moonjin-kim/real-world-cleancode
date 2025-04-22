package com.moonjin.realworld.article.port;

import com.moonjin.realworld.user.dto.response.Profile;

import java.util.Optional;

public interface UserPort {
    Profile getProfileFrom(Long userId, Long currentUserId);
}
