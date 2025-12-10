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

    // 대시보드 통계 조회
    @Transactional(readOnly = true)
    public StatsGetResponseDto getStats() {

        StatsGetResponseDto stats = dashboardRepository.getStats();

        long total = stats.getTotalTasks();
        long completed = stats.getCompletedTasks();
        long inProgress = stats.getInProgressTasks();
        long todo = stats.getTodoTasks();
        long overdue = stats.getOverdueTasks();

        long completionRate = (total == 0)
                ? 0
                : (completed * 100) / total;

        return new StatsGetResponseDto(
                total,
                completed,
                inProgress,
                todo,
                overdue,
                completionRate,
                completionRate
        );
    }
}
