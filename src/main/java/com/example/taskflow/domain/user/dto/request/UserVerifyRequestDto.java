package com.example.taskflow.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserVerifyRequestDto {

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    private String password;
}