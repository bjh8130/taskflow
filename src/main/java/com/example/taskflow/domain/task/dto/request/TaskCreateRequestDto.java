package com.example.taskflow.domain.task.dto.request;

import com.example.taskflow.domain.task.enums.TaskPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
public class TaskCreateRequestDto {
    @Size(max = 100, message = "제목은 100자 이내로 입력해주세요")
    @NotBlank(message="제목은 필수 입니다.")
    private String title;
    private String description;
    private TaskPriority priority;
    private Long assigneeId;
    private LocalDateTime dueDate;

}

