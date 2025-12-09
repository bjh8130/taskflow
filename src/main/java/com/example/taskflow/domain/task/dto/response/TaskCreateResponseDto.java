package com.example.taskflow.domain.task.dto.response;

import com.example.taskflow.domain.task.entity.Task;
import com.example.taskflow.domain.user.dto.response.UserTaskResponseDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class TaskCreateResponseDto {
    private final Long id;
    private final String title;
    private final String description;
    private final String status;
    private final String priority;

    private final Long assigneeId;
    private final UserTaskResponseDto assignee;

    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final LocalDateTime dueDate;
    public static TaskCreateResponseDto from(Task task) {
        return new TaskCreateResponseDto(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getUser().getId(),
                UserTaskResponseDto.from(task.getUser()),
                task.getCreatedAt(),
                task.getUpdatedAt(),
                task.getDueDate()
        );

    }
}
