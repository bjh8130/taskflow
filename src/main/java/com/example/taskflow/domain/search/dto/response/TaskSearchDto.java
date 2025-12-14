package com.example.taskflow.domain.search.dto.response;

import com.example.taskflow.domain.task.dto.request.TaskStatusRequestDto;
import com.example.taskflow.domain.task.entity.Task;
import com.example.taskflow.domain.task.enums.TaskStatus;
import lombok.*;

@Getter
@RequiredArgsConstructor
public class TaskSearchDto {
    private final Long id;
    private final String title;
    private final String description;
    private final TaskStatus status;

    public static TaskSearchDto from(Task task) {
        return new TaskSearchDto(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus()
        );
    }
}

