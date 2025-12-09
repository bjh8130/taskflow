package com.example.taskflow.domain.user.service;

import com.example.taskflow.common.config.PasswordEncoder;
import com.example.taskflow.domain.user.dto.request.UserCreateRequestDto;
import com.example.taskflow.domain.user.dto.response.UserCreateResponseDto;
import com.example.taskflow.domain.user.entity.User;
import com.example.taskflow.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    public UserCreateResponseDto createUser(UserCreateRequestDto request) {

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = new User(
                request.getUsername(),
                request.getEmail(),
                request.getName(),
                encodedPassword
        );

        User savedUser = userRepository.save(user);

        return UserCreateResponseDto.from(savedUser);
    }
}