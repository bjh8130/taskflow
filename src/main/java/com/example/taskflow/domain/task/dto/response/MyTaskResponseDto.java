package com.example.taskflow.domain.task.dto.response;

import com.example.taskflow.domain.task.entity.Task;
import com.example.taskflow.domain.user.dto.response.UserGetResponseDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class MyTaskResponseDto {

    private final long id;
    private final String title;
    private final UserGetResponseDto assignee;
    private final String status;
    private final String priority;
    private final LocalDateTime dueDate;

    public static MyTaskResponseDto from(Task task) {
        return new MyTaskResponseDto(
                task.getId(),
                task.getTitle(),
                UserGetResponseDto.from(task.getUser()),
                task.getStatus(),
                task.getPriority(),
                task.getDueDate()
        );
    }
}