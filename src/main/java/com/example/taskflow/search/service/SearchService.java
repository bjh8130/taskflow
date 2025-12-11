package com.example.taskflow.search.service;

import com.example.taskflow.common.exception.CustomException;
import com.example.taskflow.common.exception.ErrorCode;
import com.example.taskflow.domain.task.repository.TaskRepository;
import com.example.taskflow.domain.team.repository.TeamRepository;
import com.example.taskflow.domain.user.repository.UserRepository;
import com.example.taskflow.search.dto.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
