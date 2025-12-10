package com.example.taskflow.domain.user.controller;

import com.example.taskflow.common.response.GlobalResponse;
import com.example.taskflow.domain.user.dto.request.UserCreateRequestDto;
import com.example.taskflow.domain.user.dto.response.UserCreateResponseDto;
import com.example.taskflow.domain.user.dto.response.UserGetAllResponseDto;
import com.example.taskflow.domain.user.dto.response.UserGetResponseDto;
import com.example.taskflow.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping
    public ResponseEntity<GlobalResponse<UserCreateResponseDto>> createUser(@Valid @RequestBody UserCreateRequestDto request){
        UserCreateResponseDto result = userService.createUser(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(GlobalResponse.success(true, "회원가입이 완료되었습니다.", result));
    }

    // 사용자 정보 조회 (JWT 전까지는 다른 사용자 조회 가능)
    // TODO: Path Parameter - JWT 토큰에서 추출한 ID로 수정
    @GetMapping("/{userId}")
    public ResponseEntity<GlobalResponse<UserGetResponseDto>> getUser(@PathVariable long userId) {
        UserGetResponseDto result = userService.getUser(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(GlobalResponse.success(true, "사용자 정보 조회가 완료되었습니다.", result));
    }

    // 사용자 목록 조회
    @GetMapping
    public ResponseEntity<GlobalResponse<List<UserGetAllResponseDto>>> getAllUsers() {
        List<UserGetAllResponseDto> result = userService.getAllUsers();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(GlobalResponse.success(true, "사용자 목록 조회가 완료되었습니다.", result));
    }
}
