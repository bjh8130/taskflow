package com.example.taskflow.domain.task.service;

import com.example.taskflow.domain.task.dto.response.StatsGetResponseDto;
import com.example.taskflow.domain.task.repository.DashboardRepository;
import com.example.taskflow.domain.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final DashboardRepository dashboardRepository;
    private final TaskRepository taskRepository;

    // 대시보드 통계 조회
    @Transactional(readOnly = true)
    public StatsGetResponseDto getDashboard(long userId) {

        StatsGetResponseDto stats = dashboardRepository.getStats();

        long total = stats.getTotalTasks();
        long completed = stats.getCompletedTasks();
        long inProgress = stats.getInProgressTasks();
        long todo = stats.getTodoTasks();
        long overdue = stats.getOverdueTasks();

        // TODO: 계산식 리팩토링
        double teamProgress = (total == 0)
                ? 0.0
                : Math.round(((double) completed / total * 100.0) * 100.0) / 100.0;

        long myTotal = taskRepository.countByUserIdAndIsDeletedFalse(userId);
        long myCompleted = taskRepository.countByUserIdAndStatusAndIsDeletedFalse(userId, "DONE");
        double completionRate = (myTotal == 0)
                ? 0.0
                : Math.round(((double) myCompleted / myTotal * 100.0) * 100.0) / 100.0;

        return new StatsGetResponseDto(
                total,
                completed,
                inProgress,
                todo,
                overdue,
                teamProgress,
                completionRate
        );
    }
}
