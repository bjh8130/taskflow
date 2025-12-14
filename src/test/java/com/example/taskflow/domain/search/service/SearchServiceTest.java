
package com.example.taskflow.domain.search.service;

import com.example.taskflow.common.exception.CustomException;
import com.example.taskflow.common.exception.ErrorCode;
import com.example.taskflow.domain.search.dto.response.SearchResponseDto;
import com.example.taskflow.domain.task.entity.Task;
import com.example.taskflow.domain.task.enums.TaskPriority;
import com.example.taskflow.domain.task.enums.TaskStatus;
import com.example.taskflow.domain.task.repository.TaskRepository;
import com.example.taskflow.domain.team.entity.Team;
import com.example.taskflow.domain.team.repository.TeamRepository;
import com.example.taskflow.domain.user.entity.User;
import com.example.taskflow.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SearchService searchService;

    @Test
    void search_success() {
        // given
        String query = "test";
        User taskUser = new User("test","test@test.com", "test1","password");
        ReflectionTestUtils.setField(taskUser, "id", 5L);
        // Task 검색 결과 mock
        Task task = Task.create(
                "작업",
                "작업내용",
                taskUser,
                TaskPriority.MEDIUM,
                LocalDateTime.now()
        );
        ReflectionTestUtils.setField(task, "id", 1L);

        given(taskRepository.searchTasks(query))
                .willReturn(List.of(task));

        // Team 검색 결과 mock
        Team team = new Team("개발팀", "개발팀 설명");
        ReflectionTestUtils.setField(team, "id", 101L);

        given(teamRepository.searchTeams(query))
                .willReturn(List.of(team));

        // User 검색 결과 mock
        User user = new User("test","test@test.com", "test1","password");
        ReflectionTestUtils.setField(user, "id", 1001L);

        given(userRepository.searchUsers(query))
                .willReturn(List.of(user));

        // when
        SearchResponseDto response = searchService.search(query);

        // then
        assertThat(response.getTasks()).hasSize(1);
        assertThat(response.getTeams()).hasSize(1);
        assertThat(response.getUsers()).hasSize(1);

        assertThat(response.getTasks().get(0).getId()).isEqualTo(1L);
        assertThat(response.getTeams().get(0).getId()).isEqualTo(101L);
        assertThat(response.getUsers().get(0).getId()).isEqualTo(1001L);

        verify(taskRepository).searchTasks(query);
        verify(teamRepository).searchTeams(query);
        verify(userRepository).searchUsers(query);
    }


    @Test
    void search_invalidArgument_throwsException() {
        // given
        String emptyQuery = " ";

        // when & then
        assertThatThrownBy(() -> searchService.search(emptyQuery))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(ErrorCode.INVALID_ARGUMENT_QUERY.getMessage());

        verify(taskRepository, never()).searchTasks(anyString());
        verify(teamRepository, never()).searchTeams(anyString());
        verify(userRepository, never()).searchUsers(anyString());
    }
}