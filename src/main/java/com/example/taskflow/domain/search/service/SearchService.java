package com.example.taskflow.domain.search.service;

import com.example.taskflow.common.exception.CustomException;
import com.example.taskflow.common.exception.ErrorCode;
import com.example.taskflow.domain.search.dto.response.*;
import com.example.taskflow.domain.task.repository.TaskRepository;
import com.example.taskflow.domain.team.repository.TeamRepository;
import com.example.taskflow.domain.user.repository.UserRepository;
import com.example.taskflow.domain.search.dto.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 통합 검색 비즈니스 로직을 처리하는 서비스 클래스입니다.
 * 입력된 검색어를 기반으로 Task, Team, User 등 여러 도메인을 조회하고 결과를 통합합니다.
 * 각 도메인의 Repository를 호출하여 검색 결과를 수집한 후 SearchResponseDto로 변환하여 반환합니다.
 */
@Service
@RequiredArgsConstructor
public class SearchService {

    private final TaskRepository taskRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    public SearchResponseDto search(String query) {

        if (query == null || query.isBlank()) {
            throw new CustomException(ErrorCode.INVALID_ARGUMENT_QUERY);
        }

        List<TaskSearchDto> tasks =
                taskRepository.searchTasks(query).stream()
                        .map(TaskSearchDto::from)
                        .toList();

        List<TeamSearchDto> teams =
                teamRepository.searchTeams(query).stream()
                        .map(TeamSearchDto::from)
                        .toList();

        List<UserSearchDto> users =
                userRepository.searchUsers(query).stream()
                        .map(UserSearchDto::from)
                        .toList();

        return SearchResponseDto.from(tasks, teams, users);
    }
}
