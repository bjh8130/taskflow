package com.example.taskflow.domain.user.dto.response;

import lombok.Getter;

@Getter
public class UserActivityLogResponseDto {

    private final Long id;
    private final String username;
    private final String name;

    public UserActivityLogResponseDto(Long id, String username, String name) {
        this.id = id;
        this.username = username;
        this.name = name;
    }
}
