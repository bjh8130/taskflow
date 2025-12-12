package com.example.taskflow.domain.task.dto.request;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TaskUpdateRequestDto {
    private String title;
    private String description;
    private String status;
    private String priority;
    private Long assigneeId;
    private LocalDateTime dueDate;
}
