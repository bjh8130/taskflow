package com.example.taskflow.domain.user.service;

import com.example.taskflow.common.config.PasswordEncoder;
import com.example.taskflow.common.exception.CustomException;
import com.example.taskflow.common.exception.ErrorCode;
import com.example.taskflow.domain.user.dto.request.UserCreateRequestDto;
import com.example.taskflow.domain.user.dto.request.UserUpdateRequestDto;
import com.example.taskflow.domain.user.dto.response.UserCreateResponseDto;
import com.example.taskflow.domain.user.dto.response.UserGetAllResponseDto;
import com.example.taskflow.domain.user.dto.response.UserGetResponseDto;
import com.example.taskflow.domain.user.dto.response.UserUpdateResponseDto;
import com.example.taskflow.domain.user.entity.User;
import com.example.taskflow.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    @Transactional
    public UserCreateResponseDto createUser(UserCreateRequestDto request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new CustomException(ErrorCode.USERNAME_ALREADY_EXISTS);
        }

        if(userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ErrorCode.USER_ALREADY_EXISTS);
        }

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

    // 사용자 정보 조회
    @Transactional(readOnly = true)
    public UserGetResponseDto getUser(long userId) {

        // TODO: Soft Delete 사용자 예외 처리 추가

        User user = userRepository.findById(userId).orElseThrow(()
                -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return UserGetResponseDto.from(user);
    }

    // 사용자 목록 조회
    @Transactional(readOnly = true)
    public List<UserGetAllResponseDto> getAllUsers() {

        // TODO: Soft Delete 사용자 예외 처리 추가

        return userRepository.findAll().stream().map(UserGetAllResponseDto::from).toList();
    }

    // 사용자 정보 수정
    @Transactional
    public UserUpdateResponseDto updateUser(long userId, UserUpdateRequestDto request) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_NOT_MATCH);
        }

        if (userRepository.existsByEmailAndIdNot(request.getEmail(), userId)) {
            throw new CustomException(ErrorCode.USER_ALREADY_EXISTS);
        }

        user.update(request.getName(), request.getEmail());

        return UserUpdateResponseDto.from(user);
    }

    // 회원 탈퇴
    public void deleteUser(long userId) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );

        user.softDelete();
    }
}