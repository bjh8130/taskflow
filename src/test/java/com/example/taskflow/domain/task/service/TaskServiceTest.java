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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
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

    @DisplayName("작업 생성 성공")
    @Test
    void createTask_success() {
        // given
        Long userId = 1L;

        TaskCreateRequestDto request = new TaskCreateRequestDto();
        ReflectionTestUtils.setField(request, "title", "작업 제목");
        ReflectionTestUtils.setField(request, "description", "작업 내용");
        ReflectionTestUtils.setField(request, "assigneeId", 10L);
        ReflectionTestUtils.setField(request, "priority", null);   // default MEDIUM
        ReflectionTestUtils.setField(request, "dueDate", null);    // default now+7일

        User assignee = new User("test","test@test.com", "test1","password");
        ReflectionTestUtils.setField(assignee, "id", 10L);

        given(userRepository.findByIdAndIsDeletedFalse(10L))
                .willReturn(Optional.of(assignee));

        Task savedTask = Task.create(
                request.getTitle(),
                request.getDescription(),
                assignee,
                null,
                null
        );
        ReflectionTestUtils.setField(savedTask, "id", 100L);

        given(taskRepository.save(any(Task.class)))
                .willReturn(savedTask);

        // when
        TaskResponseDto response = taskService.createTask(request, userId);

        // then
        assertThat(response.getId()).isEqualTo(100L);
        assertThat(response.getTitle()).isEqualTo("작업 제목");
        assertThat(response.getDescription()).isEqualTo("작업 내용");
        assertThat(response.getPriority()).isEqualTo(TaskPriority.MEDIUM);
        assertThat(response.getStatus()).isEqualTo(TaskStatus.TODO);
        assertThat(response.getAssigneeId()).isEqualTo(10L);

        assertThat(response.getDueDate())
                .isAfter(LocalDateTime.now().plusDays(6))
                .isBefore(LocalDateTime.now().plusDays(8));

        verify(userRepository).findByIdAndIsDeletedFalse(10L);
        verify(taskRepository).save(any(Task.class));
    }

    @DisplayName("작업 단건조회 성공")
    @Test
    void getTaskById_success() {
        Long userId = 1L;
        Long taskId = 1L;
        User assignee = new User("test","test@test.com", "test1","password");
        ReflectionTestUtils.setField(assignee, "id", userId);

        Task task = Task.create(
                "작업",
                "작업 내용",
                assignee,
                TaskPriority.MEDIUM,
                LocalDateTime.now().plusDays(3)
        );
        ReflectionTestUtils.setField(task, "id", taskId);

        given(taskRepository.findByIdAndIsDeletedFalse(taskId))
                .willReturn(Optional.of(task));

        // when
        TaskResponseDto response = taskService.getTaskById(taskId, userId);

        // then
        assertThat(response.getId()).isEqualTo(taskId);
        assertThat(response.getTitle()).isEqualTo("작업");
        assertThat(response.getDescription()).isEqualTo("작업 내용");
        assertThat(response.getStatus()).isEqualTo(TaskStatus.TODO);
        assertThat(response.getPriority()).isEqualTo(TaskPriority.MEDIUM);
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

    @DisplayName("작업 목록 조회 - status 파라미터가 있으면 상태별 필터링")
    @Test
    void getAllTask_withStatus_returnsFiltered() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        TaskStatus status = TaskStatus.IN_PROGRESS;

        User user = new User("test", "test@test.com", "test1", "password");
        ReflectionTestUtils.setField(user, "id", 10L);

        Task task = Task.create(
                "작업",
                "작업 내용",
                user,
                TaskPriority.MEDIUM,
                LocalDateTime.now().plusDays(3)
        );
        // 상태 변경
        task.updateStatus(status);
        ReflectionTestUtils.setField(task, "id", 1L);

        Page<Task> taskPage = new PageImpl<>(List.of(task), pageable, 1);

        given(taskRepository.findAllByStatusAndIsDeletedFalse(eq(status), eq(pageable)))
                .willReturn(taskPage);

        // when
        Page<TaskResponseDto> response = taskService.getAllTask(pageable, status);

        // then
        assertThat(response.getTotalElements()).isEqualTo(1);
        assertThat(response.getContent().get(0).getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);

        verify(taskRepository).findAllByStatusAndIsDeletedFalse(status, pageable);
        verify(taskRepository, never()).findAllByIsDeletedFalse(any());
    }

    @DisplayName("작업 목록 조회 - status 파라미터가 null이면 전체 조회")
    @Test
    void getAllTask_withoutStatus_returnsAll() {
        // given
        Pageable pageable = PageRequest.of(0, 10);

        User user = new User("test", "test@test.com", "test1", "password");
        ReflectionTestUtils.setField(user, "id", 10L);

        Task task = Task.create(
                "작업",
                "작업 내용",
                user,
                TaskPriority.MEDIUM,
                LocalDateTime.now().plusDays(3)
        );
        ReflectionTestUtils.setField(task, "id", 1L);

        Page<Task> taskPage = new PageImpl<>(List.of(task), pageable, 1);

        given(taskRepository.findAllByIsDeletedFalse(pageable))
                .willReturn(taskPage);

        // when
        Page<TaskResponseDto> response = taskService.getAllTask(pageable, null);

        // then
        assertThat(response.getTotalElements()).isEqualTo(1);
        assertThat(response.getContent().get(0).getId()).isEqualTo(1L);
        assertThat(response.getContent().get(0).getStatus()).isEqualTo(TaskStatus.TODO);

        verify(taskRepository).findAllByIsDeletedFalse(pageable);
        verify(taskRepository, never()).findAllByStatusAndIsDeletedFalse(any(), any());
    }

    @Test
    void updateTask_success() {
        // given
        Long taskId = 1L;
        Long assigneeId = 10L;

        TaskUpdateRequestDto request = new TaskUpdateRequestDto();
        ReflectionTestUtils.setField(request, "title", "작업 수정");
        ReflectionTestUtils.setField(request, "description", "작업 수정 내용");
        ReflectionTestUtils.setField(request, "priority", TaskPriority.HIGH);
        ReflectionTestUtils.setField(request, "assigneeId", assigneeId);
        ReflectionTestUtils.setField(request, "dueDate", LocalDateTime.now().plusDays(3));

        // 기존 Task
        User user = new User("test","test@test.com", "test1","password");
        ReflectionTestUtils.setField(user, "id", 10L);

        Task task = Task.create(
                "작업 수정",
                "작업 수정 내용",
                user,
                TaskPriority.MEDIUM,
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
        assertThat(response.getPriority()).isEqualTo(TaskPriority.HIGH);
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

        Task task = Task.create(
                "작업 수정",
                "작업 수정 내용",
                user,
                TaskPriority.MEDIUM,
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

        Task task = Task.create(
                "작업 수정",
                "작업 수정 내용",
                anotherUser,
                TaskPriority.MEDIUM,
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
        ReflectionTestUtils.setField(request, "status", TaskStatus.IN_PROGRESS);

        User user = new User("test","test@test.com", "test1","password");
        ReflectionTestUtils.setField(user, "id", 10L);

        Task task = Task.create(
                "작업 수정",
                "작업 수정 내용",
                user,
                TaskPriority.MEDIUM,
                LocalDateTime.now().plusDays(1)
        );

        ReflectionTestUtils.setField(task, "id", taskId);

        given(taskRepository.findByIdAndIsDeletedFalse(taskId))
                .willReturn(Optional.of(task));

        // when
        TaskResponseDto response = taskService.updateTaskStatus(taskId, request);

        // then
        assertThat(response.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
    }

    @DisplayName("작업 삭제, 이미 삭제된 작업이면 아무 동작도 하지 않는다 (멱등성)")
    @Test
    void deleteTask_alreadyDeleted_doNothing() {
        // given
        Long taskId = 1L;
        Long userId = 10L;

        User user = new User("test","test@test.com", "test1","password");
        ReflectionTestUtils.setField(user, "id", userId);

        Task task = Task.create(
                "작업",
                "내용",
                user,
                TaskPriority.MEDIUM,
                LocalDateTime.now().plusDays(1)
        );
        ReflectionTestUtils.setField(task, "id", taskId);

        // 이미 삭제된 상태
        task.softDelete();

        given(userRepository.findByIdAndIsDeletedFalse(userId))
                .willReturn(Optional.of(user));
        given(taskRepository.findByIdAndIsDeletedFalse(taskId))
                .willReturn(Optional.of(task));

        // when
        taskService.deleteTask(taskId, userId);

        // then
        assertThat(task.isDeleted()).isTrue();
        verify(taskRepository, never()).save(any());
    }
}
