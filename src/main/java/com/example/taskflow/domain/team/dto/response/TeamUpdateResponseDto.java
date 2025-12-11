package com.example.taskflow.domain.team.dto.response;

import com.example.taskflow.domain.user.dto.response.UserTeamResponseDto;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class TeamUpdateResponseDto {

    private final Long id;
    private final String name;
    private final String description;
    private final LocalDateTime createdAt;
    private final List<UserTeamResponseDto> members;

    public TeamUpdateResponseDto(Long id, String name, String description, LocalDateTime createdAt, List<UserTeamResponseDto> members) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
        this.members = members;
    }
}
