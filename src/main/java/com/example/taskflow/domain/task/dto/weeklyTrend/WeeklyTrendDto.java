package com.example.taskflow.domain.task.dto.weeklyTrend;

public record WeeklyTrendDto(
        String name,
        int tasks,
        int completed,
        String date
) {}

