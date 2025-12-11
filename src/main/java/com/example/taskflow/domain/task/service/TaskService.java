package com.example.taskflow.domain.task.service;

import com.example.taskflow.common.exception.*;
import com.example.taskflow.domain.task.dto.request.*;
import com.example.taskflow.domain.task.dto.response.TaskResponseDto;
import com.example.taskflow.domain.task.entity.Task;
import com.example.taskflow.domain.task.enums.TaskPriority;
import com.example.taskflow.domain.task.enums.TaskStatus;
import com.example.taskflow.domain.task.repository.TaskRepository;
import com.example.taskflow.domain.user.entity.User;
import com.example.taskflow.domain.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Transactional
    public TaskResponseDto createTask(TaskCreateRequestDto request) {
        Long userId= 1L;
        User user = userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Task task = new Task(
                request.getTitle(),
                request.getDescription(),
                TaskStatus.TODO.name(),
                request.getPriority() != null
                        ? request.getPriority()
                        : TaskPriority.MEDIUM.name(),
                user,
                request.getDueDate() != null
                        ? request.getDueDate()
                        :LocalDateTime.now().plusDays(7)
        );
        Task savedTask = taskRepository.save(task);
        return TaskResponseDto.from(savedTask, false);
    }
    @Transactional(readOnly=true)
    public TaskResponseDto getTaskById(Long taskId) {
        Task task = taskRepository.findByIdAndIsDeletedFalse(taskId)
                .orElseThrow(() -> new CustomException(ErrorCode.TASK_NOT_FOUND));
        return TaskResponseDto.from(task, true);
    }

    @Transactional(readOnly=true)
    public Page<TaskResponseDto> getAllTask(Pageable pageable, String status) {
        Page<Task> tasks;
        if(status == null) {
            tasks = taskRepository.findAllByIsDeletedFalse(pageable);
        } else {
            if(!TaskStatus.isValid(status)) {
                throw new CustomException(ErrorCode.INVALID_ARGUMENT_STATUS);
            }
            tasks = taskRepository.findAllByStatusAndIsDeletedFalse(status,pageable);
        }
        return tasks.map(task -> TaskResponseDto.from(task, false));
    }

    @Transactional
    public TaskResponseDto updateTask(Long taskId, TaskUpdateRequestDto request) {
        Task task = taskRepository.findByIdAndIsDeletedFalse(taskId)
                .orElseThrow(() -> new CustomException(ErrorCode.TASK_NOT_FOUND));
        //TODO 인증인가 구현 후 수정 권한 예외처리 예정

        task.update(
                request.getTitle(),
                request.getDescription(),
                request.getPriority(),
                request.getDueDate()
        );
        return TaskResponseDto.from(task, false);
    }

    @Transactional
    public void deleteTask(Long taskId) {
        Long userId= 1L;
        User user = userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Task task = taskRepository.findByIdAndIsDeletedFalse(taskId)
                .orElseThrow(() -> new CustomException(ErrorCode.TASK_NOT_FOUND));

        if (!task.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
        // 이미 삭제된 상태라면 그냥 종료 (멱등성 보장)
        if (task.isDeleted()) {
            return; // 아무 동작 X → 멱등성 유지
        }
        task.softDelete();
    }

    @Transactional
    public TaskResponseDto updateTaskStatus(Long taskId, TaskStatusRequestDto request) {
        if(!TaskStatus.isValid(request.getStatus())) {
            throw new CustomException(ErrorCode.INVALID_ARGUMENT_STATUS);
        }
        Task task = taskRepository.findByIdAndIsDeletedFalse(taskId)
                .orElseThrow(() -> new CustomException(ErrorCode.TASK_NOT_FOUND));
        task.updateStatus(request.getStatus());
        return TaskResponseDto.from(task, false);
    }
}
