package com.example.taskflow.domain.user.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class UserVerifyResponseDto {

    private final boolean valid;

    public UserVerifyResponseDto(boolean valid) {
        this.valid = valid;
    }
}