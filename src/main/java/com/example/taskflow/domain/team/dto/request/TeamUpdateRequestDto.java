package com.example.taskflow.domain.team.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class TeamUpdateRequestDto {

    @NotBlank(message = "팀 이름은 필수입니다.")
    @Size(min = 1, max = 50, message = "팀 이름은 50자를 초과할 수 없습니다.")
    private String name;

    private String description;
}
