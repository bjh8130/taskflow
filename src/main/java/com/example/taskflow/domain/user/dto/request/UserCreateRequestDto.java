package com.example.taskflow.domain.user.dto.request;

import lombok.Getter;

@Getter
public class UserCreateRequestDto {

    private String username;
    private String email;
    private String password;
    private String name;
}
