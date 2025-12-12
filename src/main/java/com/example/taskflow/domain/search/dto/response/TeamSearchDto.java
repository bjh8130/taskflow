package com.example.taskflow.domain.search.dto.response;

import com.example.taskflow.domain.team.entity.Team;
import lombok.*;

@Getter
@RequiredArgsConstructor
public class TeamSearchDto {
    private final Long id;
    private final String name;
    private final String description;

    public static TeamSearchDto from(Team team) {
        return new  TeamSearchDto(
                team.getId(),
                team.getName(),
                team.getDescription()
        );
    }
}
