package com.moonjin.realworld.user.service;

import com.moonjin.realworld.common.exception.AlreadyExistsEmailException;
import com.moonjin.realworld.common.exception.Unauthorized;
import com.moonjin.realworld.user.domain.User;
import com.moonjin.realworld.user.dto.request.Signin;
import com.moonjin.realworld.user.dto.request.Signup;
import com.moonjin.realworld.user.dto.response.AuthResponse;
import com.moonjin.realworld.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private static final String SESSION_USER_KEY = "user";

    public AuthResponse signup(Signup request) {
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

        return AuthResponse.of(user);
    }

    public AuthResponse signin(Signin request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(Unauthorized::new);
        if (user.authNotPass(request.getPassword())) {
            throw new Unauthorized();
        }

        return AuthResponse.of(user);
    }
}
