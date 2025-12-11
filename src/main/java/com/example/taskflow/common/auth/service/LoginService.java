package com.example.taskflow.common.auth.service;

import com.example.taskflow.common.auth.dto.request.LoginRequestDto;
import com.example.taskflow.common.auth.dto.response.LoginResponseDto;
import com.example.taskflow.common.auth.util.JwtUtil;
import com.example.taskflow.common.config.PasswordEncoder;
import com.example.taskflow.common.exception.CustomException;
import com.example.taskflow.common.exception.ErrorCode;
import com.example.taskflow.domain.user.entity.User;
import com.example.taskflow.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public LoginResponseDto login(LoginRequestDto requestDto) {
        User user = userRepository.findByUsername(requestDto.getUsername())
                .orElseThrow(()-> new CustomException(ErrorCode.LOGIN_UNAUTHORIZED));
        boolean matchPassword = passwordEncoder.matches(requestDto.getPassword(), user.getPassword());

        if(!matchPassword) {
            new CustomException(ErrorCode.LOGIN_UNAUTHORIZED);
        }
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getEmail(), user.getRole());
       return new LoginResponseDto(token);
    }
}
