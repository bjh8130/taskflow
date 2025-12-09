package com.example.taskflow.domain.user.service;

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

    // 회원가입
    public UserCreateResponseDto createUser(UserCreateRequestDto request) {

        User user = new User(
                request.getUsername(),
                request.getEmail(),
                request.getPassword(),
                request.getName()
        );

        User savedUser = userRepository.save(user);

        return UserCreateResponseDto.from(savedUser);
    }
}