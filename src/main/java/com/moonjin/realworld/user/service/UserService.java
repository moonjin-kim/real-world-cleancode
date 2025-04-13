package com.moonjin.realworld.user.service;

import com.moonjin.realworld.common.exception.AlreadyExistsEmailException;
import com.moonjin.realworld.user.domain.User;
import com.moonjin.realworld.user.dto.request.SignupRequest;
import com.moonjin.realworld.user.dto.response.AuthResponse;
import com.moonjin.realworld.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public AuthResponse signup(SignupRequest request) {
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
}
