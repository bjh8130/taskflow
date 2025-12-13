package com.example.taskflow.domain.task.service;

import com.example.taskflow.domain.task.dto.request.TaskCreateRequestDto;
import com.example.taskflow.domain.task.dto.response.TaskResponseDto;
import com.example.taskflow.domain.task.entity.Task;
import com.example.taskflow.domain.task.enums.TaskPriority;
import com.example.taskflow.domain.task.enums.TaskStatus;
import com.example.taskflow.domain.task.repository.TaskRepository;
import com.example.taskflow.domain.user.entity.User;
import com.example.taskflow.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void createTask_success() {
        // given
        Long userId = 1L;

        TaskCreateRequestDto request = new TaskCreateRequestDto();
        ReflectionTestUtils.setField(request, "title", "Test Task");
        ReflectionTestUtils.setField(request, "description", "Test Description");
        ReflectionTestUtils.setField(request, "assigneeId", 10L);
        ReflectionTestUtils.setField(request, "priority", null);   // default MEDIUM
        ReflectionTestUtils.setField(request, "dueDate", null);    // default now+7일

        User assignee = new User("test","test@test.com", "test1","password");
        ReflectionTestUtils.setField(assignee, "id", 10L);

        given(userRepository.findByIdAndIsDeletedFalse(10L))
                .willReturn(Optional.of(assignee));

        Task savedTask = new Task(
                request.getTitle(),
                request.getDescription(),
                TaskStatus.TODO.name(),
                TaskPriority.MEDIUM.name(), // default
                assignee,
                LocalDateTime.now().plusDays(7)
        );
        ReflectionTestUtils.setField(savedTask, "id", 100L);

        given(taskRepository.save(any(Task.class)))
                .willReturn(savedTask);

        // when
        TaskResponseDto response = taskService.createTask(request, userId);

        // then
        assertThat(response.getId()).isEqualTo(100L);
        assertThat(response.getTitle()).isEqualTo("Test Task");
        assertThat(response.getDescription()).isEqualTo("Test Description");
        assertThat(response.getPriority()).isEqualTo(TaskPriority.MEDIUM.name());
        assertThat(response.getStatus()).isEqualTo(TaskStatus.TODO.name());
        assertThat(response.getAssigneeId()).isEqualTo(10L);

        assertThat(response.getDueDate())
                .isAfter(LocalDateTime.now().plusDays(6))
                .isBefore(LocalDateTime.now().plusDays(8));

        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void getTaskById_success() {
        Long userId = 1L;
        Long taskId = 1L;
        User assignee = new User("test","test@test.com", "test1","password");
        ReflectionTestUtils.setField(assignee, "id", userId);

        Task task = new Task(
                "Test Task",
                "Test Description",
                TaskStatus.TODO.name(),
                TaskPriority.MEDIUM.name(),
                assignee,
                LocalDateTime.now().plusDays(3)
        );
        ReflectionTestUtils.setField(task, "id", taskId);

        given(taskRepository.findByIdAndIsDeletedFalse(taskId))
                .willReturn(Optional.of(task));

        // when
        TaskResponseDto response = taskService.getTaskById(taskId, userId);

        // then
        assertThat(response.getId()).isEqualTo(taskId);
        assertThat(response.getTitle()).isEqualTo("Test Task");
        assertThat(response.getDescription()).isEqualTo("Test Description");
        assertThat(response.getStatus()).isEqualTo(TaskStatus.TODO.name());
        assertThat(response.getPriority()).isEqualTo(TaskPriority.MEDIUM.name());
        assertThat(response.getAssigneeId()).isEqualTo(userId);

        verify(taskRepository, times(1))
                .findByIdAndIsDeletedFalse(taskId);
    }

    }
