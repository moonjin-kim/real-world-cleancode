package com.moonjin.realworld.service;

import com.moonjin.realworld.common.exception.AlreadyExistsEmailException;
import com.moonjin.realworld.common.exception.Unauthorized;
import com.moonjin.realworld.common.exception.UserNotFoundException;
import com.moonjin.realworld.domain.user.User;
import com.moonjin.realworld.dto.request.PutUser;
import com.moonjin.realworld.dto.request.Signin;
import com.moonjin.realworld.dto.request.Signup;
import com.moonjin.realworld.dto.response.Profile;
import com.moonjin.realworld.dto.response.UserDetail;
import com.moonjin.realworld.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User signup(Signup request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
        if (userOptional.isPresent()) {
            throw new AlreadyExistsEmailException();
        }

        User user = User.of(request);
        userRepository.save(user);

        return user;
    }

    @Transactional
    public User signIn(Signin request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(Unauthorized::new);
        if (user.authNotPass(request.getPassword())) {
            throw new Unauthorized();
        }

        return user;
    }

    @Transactional
    public UserDetail put(Long userId, PutUser request) {
        User findUser = userRepository.findById(userId).orElseThrow(Unauthorized::new);

        findUser.patch(request);
        return UserDetail.of(findUser);
    }

    @Transactional(readOnly = true)
    public UserDetail getUserDetail(Long userId) {
        User findUser = userRepository.findById(userId).orElseThrow(Unauthorized::new);

        return UserDetail.of(findUser);
    }


    @Transactional(readOnly = true)
    public Profile getProfileFrom(String username, Long loginUserId) {
        User findUser = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);

        boolean isFollowing = false;
        if(loginUserId != null) {
            User loginUser = userRepository.findById(loginUserId).orElseThrow(UserNotFoundException::new);

            isFollowing = loginUser.isFollowing(findUser);
        }
        return Profile.of(findUser, isFollowing);
    }

    @Transactional
    public Profile follow(Long userId, String username) {
        User follower = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        User followee = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        follower.follow(followee);
        userRepository.save(follower);

        return Profile.of(followee, true);
    }

    @Transactional
    public Profile unFollow(Long userId, String username) {
        User follower = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        User followee = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        follower.unfollow(followee);
        userRepository.save(follower);

        return Profile.of(followee, false);
    }
}
