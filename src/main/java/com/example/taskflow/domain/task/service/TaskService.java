package com.example.taskflow.domain.task.service;

import com.example.taskflow.common.exception.*;
import com.example.taskflow.domain.task.dto.request.TaskCreateRequestDTO;
import com.example.taskflow.domain.task.dto.response.TaskResponseDto;
import com.example.taskflow.domain.task.entity.Task;
import com.example.taskflow.domain.task.enums.TaskPriority;
import com.example.taskflow.domain.task.enums.TaskStatus;
import com.example.taskflow.domain.task.repository.TaskRepository;
import com.example.taskflow.domain.user.entity.User;
import com.example.taskflow.domain.user.repository.UserRepository;
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
    public TaskResponseDto createTask(TaskCreateRequestDTO request) {
        Long userId= 1L;
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.TASK_NOT_FOUND));
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
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new CustomException(ErrorCode.TASK_NOT_FOUND));
        return TaskResponseDto.from(task, true);
    }
}
