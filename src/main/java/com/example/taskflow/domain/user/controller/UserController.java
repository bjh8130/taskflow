package com.example.taskflow.domain.user.controller;

import com.example.taskflow.common.response.GlobalResponse;
import com.example.taskflow.domain.user.dto.request.UserCreateRequestDto;
import com.example.taskflow.domain.user.dto.response.UserCreateResponseDto;
import com.example.taskflow.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/api/users")
    public ResponseEntity<GlobalResponse<UserCreateResponseDto>> createUser(@Valid @RequestBody UserCreateRequestDto request){
        UserCreateResponseDto result = userService.createUser(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(GlobalResponse.success(true, "회원가입이 완료되었습니다.", result));
    }
}