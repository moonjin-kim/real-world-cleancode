package com.moonjin.realworld.user.service;

import com.moonjin.realworld.common.exception.AlreadyExistsEmailException;
import com.moonjin.realworld.common.exception.Unauthorized;
import com.moonjin.realworld.user.domain.User;
import com.moonjin.realworld.user.dto.request.PutRequest;
import com.moonjin.realworld.user.dto.request.Signin;
import com.moonjin.realworld.user.dto.request.Signup;
import com.moonjin.realworld.user.dto.response.UserDetail;
import com.moonjin.realworld.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private static final String SESSION_USER_KEY = "user";

    @Transactional
    public User signup(Signup request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
        if (userOptional.isPresent()) {
            throw new AlreadyExistsEmailException();
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .username(request.getUsername())
                .build();
        userRepository.save(user);

        return user;
    }

    @Transactional
    public User signin(Signin request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(Unauthorized::new);
        if (user.authNotPass(request.getPassword())) {
            throw new Unauthorized();
        }

        return user;
    }

    @Transactional
    public UserDetail put(User user, PutRequest request) {
        User findUser = userRepository.findById(user.getId()).orElseThrow(Unauthorized::new);

        if(request.getEmail() != null) {
            findUser.putEmail(request.getEmail());
        }

        if(request.getPassword() != null) {
            findUser.putPassword(request.getPassword());
        }

        if(request.getBio() != null) {
            findUser.putBio(request.getBio());
        }

        if(request.getImage() != null) {
            findUser.putImage(request.getImage());
        }

        if(request.getUsername() != null) {
            findUser.putUsername(request.getUsername());
        }

        return UserDetail.of(findUser);
    }
}
