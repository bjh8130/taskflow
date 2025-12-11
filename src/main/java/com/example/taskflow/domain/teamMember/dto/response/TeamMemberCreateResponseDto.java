package com.example.taskflow.domain.teamMember.dto.response;

import com.example.taskflow.domain.user.dto.response.UserTeamResponseDto;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class TeamMemberCreateResponseDto {

    private final Long teamId;
    private final String name;
    private final String description;
    private final LocalDateTime createdAt;
    private final List<UserTeamResponseDto> members;

    public TeamMemberCreateResponseDto(Long teamId, String name, String description, LocalDateTime createdAt, List<UserTeamResponseDto> members) {
        this.teamId = teamId;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
        this.members = members;
    }
}
