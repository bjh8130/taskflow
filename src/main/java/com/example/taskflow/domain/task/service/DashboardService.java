package com.example.taskflow.domain.task.service;

import com.example.taskflow.common.auth.util.ProgressCalculator;
import com.example.taskflow.common.exception.CustomException;
import com.example.taskflow.common.exception.ErrorCode;
import com.example.taskflow.domain.task.dto.response.MyTaskGetResponseDto;
import com.example.taskflow.domain.task.dto.response.MyTaskResponseDto;
import com.example.taskflow.domain.task.dto.response.StatsGetResponseDto;
import com.example.taskflow.domain.task.dto.weeklyTrend.WeeklyTrendDto;
import com.example.taskflow.domain.task.entity.Task;
import com.example.taskflow.domain.task.enums.TaskStatus;
import com.example.taskflow.domain.task.repository.DashboardRepository;
import com.example.taskflow.domain.task.repository.TaskRepository;
import com.example.taskflow.domain.teamMember.repository.TeamMemberRepository;
import com.example.taskflow.domain.user.entity.User;
import com.example.taskflow.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final DashboardRepository dashboardRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TeamMemberRepository teamMemberRepository;

    // 대시보드 통계 조회
    @Transactional(readOnly = true)
    public StatsGetResponseDto getDashboard(long userId) {

        StatsGetResponseDto stats = dashboardRepository.getStats();

        long total = stats.getTotalTasks();
        long completed = stats.getCompletedTasks();
        long inProgress = stats.getInProgressTasks();
        long todo = stats.getTodoTasks();
        long overdue = stats.getOverdueTasks();

        Long teamId = teamMemberRepository.findTeamIdByUserId(userId);
        long teamTotal = taskRepository.countTeamTotal(teamId);
        long teamCompleted = taskRepository.countTeamCompleted(teamId);
        double teamProgress = ProgressCalculator.calculate(teamTotal, teamCompleted);

        long myTotal = taskRepository.countByUserIdAndIsDeletedFalse(userId);
        long myCompleted = taskRepository.countByUserIdAndStatusAndIsDeletedFalse(userId, TaskStatus.DONE.name());
        double completionRate = ProgressCalculator.calculate(myTotal, myCompleted);

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

    // 내 작업 요약 조회
    @Transactional(readOnly = true)
    public MyTaskGetResponseDto getMyTasks(long userId) {

        User user = userRepository.findByIdAndIsDeletedFalse(userId).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );

        LocalDate now = LocalDate.now();
        LocalDateTime start = now.atStartOfDay();
        LocalDateTime end = now.plusDays(1).atStartOfDay();

        List<MyTaskResponseDto> todayTasks = taskRepository
                .findAllByUserIdAndIsDeletedFalseAndDueDateGreaterThanEqualAndDueDateLessThan(userId, start, end)
                .stream().map(MyTaskResponseDto::from).toList();

        List<MyTaskResponseDto> upcomingTasks = taskRepository
                .findAllByUserIdAndIsDeletedFalseAndDueDateGreaterThanEqual(userId, end)
                .stream().map(MyTaskResponseDto::from).toList();

        List<MyTaskResponseDto> overdueTasks = taskRepository
                .findAllByUserIdAndIsDeletedFalseAndDueDateLessThanAndStatusNot(userId, start, "DONE")
                .stream().map(MyTaskResponseDto::from).toList();

        return new MyTaskGetResponseDto(todayTasks, upcomingTasks, overdueTasks);
    }

    /**
     * 12.11 구현 - 성주연
     * 12.12 리팩토링 - 성주연
     * 이월 방식 작업 주간 추세
     * - tasks: 전날 미완료 + 오늘 생성
     * - completed: 오늘 완료한 작업
     * - 미완료 = tasks - completed (다음 날로 이월)
     */
    @Transactional(readOnly = true)
    public List<WeeklyTrendDto> getWeeklyTrend() {
        LocalDate today = LocalDate.now();

        List<WeeklyTrendDto> result = new ArrayList<>();
        int previousIncomplete = 0;  // 전날 미완료 작업 수

        // 7일 전부터 오늘까지 순서대로 계산
        for (int i = 6; i >= 0; i--) {
            LocalDate date = today.minusDays(i);

            // 그날 생성된 작업 수
            List<Task> createdTasks = taskRepository.findByCreatedAtBetween(
                    date.atStartOfDay(),
                    date.atTime(23, 59, 59)
            );
            int createdCount = createdTasks.size();

            // 그날 완료된 작업 수 (언제 만들었든 상관없이!!!!)
            List<Task> completedTasks = taskRepository.findByCompletedDateBetweenAndIsDeletedFalse(
                    date.atStartOfDay(),
                    date.atTime(23, 59, 59)
            );
            int completedCount = completedTasks.size();

            // tasks = 전날 미완료 + 오늘 생성
            int totalTasks = previousIncomplete + createdCount;

            // 미완료 작업 = tasks - 오늘 완료
            int incomplete = totalTasks - completedCount;

            // DTO 생성
            result.add(new WeeklyTrendDto(
                    getKoreanDayName(date),
                    totalTasks,        // 전날 미완료 + 오늘 생성
                    completedCount,    // 오늘 완료한 작업
                    date.toString()
            ));

            // 다음 날로 이월
            previousIncomplete = incomplete;
        }

        // 7일 전부터 오늘까지 시간 순서대로 반환 (과거 → 현재)
        return result;
    }

    // 날짜를 요일 한글로 변환
    private String getKoreanDayName(LocalDate date) {
        return date.getDayOfWeek().getDisplayName(TextStyle.NARROW, Locale.KOREAN);
    }
}
