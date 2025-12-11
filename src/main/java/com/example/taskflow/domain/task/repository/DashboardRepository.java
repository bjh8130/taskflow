package com.example.taskflow.domain.task.repository;

import com.example.taskflow.domain.task.dto.response.StatsGetResponseDto;

public interface DashboardRepository {

    StatsGetResponseDto getStats();
}
