package com.example.taskflow.domain.activityLog.repository;

import com.example.taskflow.domain.activityLog.entity.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
}
