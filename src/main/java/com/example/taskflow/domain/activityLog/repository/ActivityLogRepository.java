package com.example.taskflow.domain.activityLog.repository;

import com.example.taskflow.domain.activityLog.entity.ActivityLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {

    @Query("""
            SELECT a FROM ActivityLog a
            WHERE (:type IS NULL OR a.type = :type)
              AND (:userId IS NULL OR a.user.id = :userId)
              AND (:taskId IS NULL OR a.taskId = :taskId)
              AND (:startDate IS NULL OR a.createdAt >= :startDate)
              AND (:endDate IS NULL OR a.createdAt <= :endDate)
            """)
    Page<ActivityLog> search(
            Pageable pageable,
            @Param("type") String type,
            @Param("userId") Long userId,
            @Param("taskId") Long taskId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    List<ActivityLog> findByUserId(Long userId);
}
