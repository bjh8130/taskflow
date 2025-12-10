package com.example.taskflow.domain.team.dto.response;

import com.example.taskflow.domain.user.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class TeamReadResponseDto {

    private final Long id;
    private final String name;
    private final String description;
    private final LocalDateTime createdAt;
    private final List<User> members;

    public TeamReadResponseDto(Long id, String name, String description, LocalDateTime createdAt, List<User> members) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
        this.members = members;
    }
}
