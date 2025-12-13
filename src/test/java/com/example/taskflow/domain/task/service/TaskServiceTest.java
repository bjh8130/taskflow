package com.example.taskflow.domain.task.service;

import com.example.taskflow.common.exception.CustomException;
import com.example.taskflow.common.exception.ErrorCode;
import com.example.taskflow.domain.task.dto.request.*;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

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

    @Test
    void getTaskById_taskNotFound() {
        // given
        Long taskId = 1L;
        Long userId = 10L;

        given(taskRepository.findByIdAndIsDeletedFalse(taskId))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> taskService.getTaskById(taskId, userId))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(ErrorCode.TASK_NOT_FOUND.getMessage());

        verify(taskRepository, times(1))
                .findByIdAndIsDeletedFalse(taskId);
    }

    @Test
    void getAllTask_invalidStatus_throwsException() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        String invalidStatus = "NOT_EXIST";

        // when & then
        assertThatThrownBy(() -> taskService.getAllTask(pageable, invalidStatus))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(ErrorCode.INVALID_ARGUMENT_STATUS.getMessage());

        verify(taskRepository, never())
                .findAllByStatusAndIsDeletedFalse(anyString(), any());
        verify(taskRepository, never())
                .findAllByIsDeletedFalse(any());
    }

    @Test
    void updateTask_success() {
        // given
        Long taskId = 1L;
        Long assigneeId = 10L;

        TaskUpdateRequestDto request = new TaskUpdateRequestDto();
        ReflectionTestUtils.setField(request, "title", "작업 수정");
        ReflectionTestUtils.setField(request, "description", "작업 수정 내용");
        ReflectionTestUtils.setField(request, "priority", TaskPriority.HIGH.name());
        ReflectionTestUtils.setField(request, "assigneeId", assigneeId);
        ReflectionTestUtils.setField(request, "dueDate", LocalDateTime.now().plusDays(3));

        // 기존 Task
        User user = new User("test","test@test.com", "test1","password");
        ReflectionTestUtils.setField(user, "id", 99L);

        Task task = new Task(
                "Old Title",
                "Old Desc",
                TaskStatus.TODO.name(),
                TaskPriority.MEDIUM.name(),
                user,
                LocalDateTime.now().plusDays(1)
        );
        ReflectionTestUtils.setField(task, "id", taskId);

        // 새로운 담당자
        User newUser = new User("test1","test1@test.com", "test2","password");
        ReflectionTestUtils.setField(newUser, "id", assigneeId);

        given(taskRepository.findByIdAndIsDeletedFalse(taskId))
                .willReturn(Optional.of(task));

        given(userRepository.findByIdAndIsDeletedFalse(assigneeId))
                .willReturn(Optional.of(newUser));

        // when
        TaskResponseDto response = taskService.updateTask(taskId, request);

        // then
        assertThat(response.getTitle()).isEqualTo("작업 수정");
        assertThat(response.getDescription()).isEqualTo("작업 수정 내용");
        assertThat(response.getPriority()).isEqualTo(TaskPriority.HIGH.name());
        assertThat(response.getAssigneeId()).isEqualTo(assigneeId);

        verify(taskRepository).findByIdAndIsDeletedFalse(taskId);
        verify(userRepository).findByIdAndIsDeletedFalse(assigneeId);
    }

    @Test
    void updateTask_taskNotFound_throwsException() {
        // given
        Long taskId = 1L;
        TaskUpdateRequestDto request = new TaskUpdateRequestDto();

        given(taskRepository.findByIdAndIsDeletedFalse(taskId))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> taskService.updateTask(taskId, request))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(ErrorCode.TASK_NOT_FOUND.getMessage());
    }

    @Test
    void deleteTask_success() {
        // given
        Long taskId = 1L;
        Long userId = 99L;

        User user = new User("test","test@test.com", "test1","password");
        ReflectionTestUtils.setField(user, "id", userId);

        Task task = new Task(
                "작업",
                "작업내용",
                TaskStatus.TODO.name(),
                TaskPriority.MEDIUM.name(),
                user,
                LocalDateTime.now().plusDays(1)
        );
        ReflectionTestUtils.setField(task, "id", taskId);

        given(userRepository.findByIdAndIsDeletedFalse(userId))
                .willReturn(Optional.of(user));

        given(taskRepository.findByIdAndIsDeletedFalse(taskId))
                .willReturn(Optional.of(task));

        // when
        taskService.deleteTask(taskId, userId);

        // then
        assertThat(task.isDeleted()).isTrue();
    }

    @Test
    void deleteTask_forbidden_throwsException() {
        // given
        Long userId = 1L;
        Long taskId = 10L;

        User requester = new User("test","test@test.com", "test1","password");
        ReflectionTestUtils.setField(requester, "id", userId);

        User anotherUser = new User("other","other@test.com", "other1","password");
        ReflectionTestUtils.setField(anotherUser, "id", 999L);

        Task task = new Task(
                "작업",
                "작업내용",
                TaskStatus.TODO.name(),
                TaskPriority.MEDIUM.name(),
                anotherUser,
                LocalDateTime.now().plusDays(1)
        );

        given(userRepository.findByIdAndIsDeletedFalse(userId))
                .willReturn(Optional.of(requester));
        given(taskRepository.findByIdAndIsDeletedFalse(taskId))
                .willReturn(Optional.of(task));

        // when & then
        assertThatThrownBy(() -> taskService.deleteTask(taskId, userId))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(ErrorCode.FORBIDDEN.getMessage());
    }

    @Test
    void updateTaskStatus_success() {
        // given
        Long taskId = 1L;

        TaskStatusRequestDto request = new TaskStatusRequestDto();
        ReflectionTestUtils.setField(request, "status", TaskStatus.IN_PROGRESS.name());

        User user = new User("test","test@test.com", "test1","password");
        ReflectionTestUtils.setField(user, "id", 10L);

        Task task = new Task(
                "작업",
                "작업내용",
                TaskStatus.TODO.name(),
                TaskPriority.MEDIUM.name(),
                user,
                LocalDateTime.now().plusDays(1)
        );

        given(taskRepository.findByIdAndIsDeletedFalse(taskId))
                .willReturn(Optional.of(task));

        // when
        TaskResponseDto response = taskService.updateTaskStatus(taskId, request);

        // then
        assertThat(response.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS.name());
    }

    @Test
    void updateTaskStatus_invalidStatus_throwsException() {
        // given
        Long taskId = 1L;

        TaskStatusRequestDto request = new TaskStatusRequestDto();
        ReflectionTestUtils.setField(request, "status", "NOT_EXIST");

        // when & then
        assertThatThrownBy(() -> taskService.updateTaskStatus(taskId, request))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(ErrorCode.INVALID_ARGUMENT_STATUS.getMessage());
    }
}
