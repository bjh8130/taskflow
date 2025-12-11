package com.example.taskflow.domain.user.dto.response;

import com.example.taskflow.domain.user.enums.UserRole;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserTeamResponseDto {

    private final Long id;
    private final String username;
    private final String name;
    private final String email;
    private final UserRole role;
    private final LocalDateTime createdAt;

    public UserTeamResponseDto(Long id, String username, String name, String email, UserRole role, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;
        this.role = role;
        this.createdAt = createdAt;
    }
}
