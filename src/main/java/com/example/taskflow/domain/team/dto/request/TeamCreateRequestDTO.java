package com.example.taskflow.domain.team.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class TeamCreateRequestDTO {

    @NotBlank(message = "팀 이름은 필수입니다.")
    private String name;

    private String description;
}