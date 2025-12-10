package com.example.taskflow.domain.task.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class StatsGetResponseDto {

    private final long totalTasks;
    private final long completedTasks;
    private final long inProgressTasks;
    private final long todoTasks;
    private final long overdueTasks;
    private final double teamProgress;
    private final double completionRate;

    public static StatsGetResponseDto from(
            long totalTasks,
            long completedTasks,
            long inProgressTasks,
            long todoTasks,
            long overdueTasks,
            double teamProgress,
            double completionRate
    ) {
        return new StatsGetResponseDto(
                totalTasks,
                completedTasks,
                inProgressTasks,
                todoTasks,
                overdueTasks,
                teamProgress,
                completionRate
        );
    }
}