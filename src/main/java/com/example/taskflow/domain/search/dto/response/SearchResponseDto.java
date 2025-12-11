package com.example.taskflow.domain.search.dto.response;

import lombok.*;

import java.util.List;

/**
 * 통합 검색 결과를 담는 응답 DTO입니다.
 * Task, Team, User 검색 결과를 각각 별도의 리스트로 구분하여 클라이언트에 전달합니다.
 * 검색 기능 확장 시 도메인별 응답 구조를 유연하게 추가할 수 있도록 설계되었습니다.
 */
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

