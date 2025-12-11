package com.example.taskflow.search.dto.response;

import lombok.*;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class SearchResponseDto {
    private final List<TaskSearchDto> tasks;
    private final List<TeamSearchDto> teams;
    private final List<UserSearchDto> users;

    public static SearchResponseDto from(
            List<TaskSearchDto> tasks,
            List<TeamSearchDto> teams,
            List<UserSearchDto> users
    ) {
        return new SearchResponseDto(tasks, teams, users);
    }
}

