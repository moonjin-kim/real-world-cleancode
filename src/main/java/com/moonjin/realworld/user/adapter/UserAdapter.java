package com.moonjin.realworld.user.adapter;

import com.moonjin.realworld.article.port.UserPort;
import com.moonjin.realworld.common.exception.UserNotFoundException;
import com.moonjin.realworld.user.domain.User;
import com.moonjin.realworld.user.dto.response.Profile;
import com.moonjin.realworld.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserAdapter implements UserPort {
    private final UserRepository userRepository;

    @Override
    public Profile getProfileFrom(Long userId, Long currentUserId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        boolean isFollowed = false;
        if(currentUserId != null) {
            isFollowed = isIsFollowed(userId, user);
        }


        return Profile.of(user, isFollowed);
    }

    private boolean isIsFollowed(Long userId, User user) {
        Optional<User> optionalUser =  userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User currentUser = optionalUser.get();
            return currentUser.isFollowing(user);
        }
        return false;
    }
}
