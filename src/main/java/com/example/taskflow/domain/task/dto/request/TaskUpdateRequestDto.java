package com.example.taskflow.domain.task.dto.request;

import com.example.taskflow.domain.task.enums.TaskPriority;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TaskUpdateRequestDto {
    private String title;
    private String description;
    private TaskPriority priority;
    private Long assigneeId;
    private LocalDateTime dueDate;
}
