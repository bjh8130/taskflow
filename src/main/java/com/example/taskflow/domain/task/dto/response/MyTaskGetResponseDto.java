package com.example.taskflow.domain.task.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class MyTaskGetResponseDto {

    private final List<MyTaskResponseDto> todayTasks;
    private final List<MyTaskResponseDto> upcomingTasks;
    private final List<MyTaskResponseDto> overdueTasks;
}
