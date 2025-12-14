package com.example.taskflow.domain.task.dto.request;

import com.example.taskflow.domain.task.enums.TaskStatus;
import lombok.Getter;

@Getter
public class TaskStatusRequestDto {
    private TaskStatus status;
}
