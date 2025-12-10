package com.example.taskflow.domain.task.repository;

import com.example.taskflow.domain.task.dto.response.StatsGetResponseDto;
import com.example.taskflow.domain.task.entity.QTask;
import com.example.taskflow.domain.task.entity.Task;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class DashboardQueryRepository implements DashboardRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public StatsGetResponseDto getStats() {

        QTask task = QTask.task;
        LocalDateTime now = LocalDateTime.now();

        var completed = new CaseBuilder()
                .when(task.status.eq("DONE")).then(1L)
                .otherwise(0L)
                .sum();

        var inProgress = new CaseBuilder()
                .when(task.status.eq("IN_PROGRESS")).then(1L)
                .otherwise(0L)
                .sum();

        var todo = new CaseBuilder()
                .when(task.status.eq("TODO")).then(1L)
                .otherwise(0L)
                .sum();

        var overdue = new CaseBuilder()
                .when(task.dueDate.before(now)
                        .and(task.status.ne("DONE")))
                .then(1L)
                .otherwise(0L)
                .sum();

        StatsGetResponseDto stats = jpaQueryFactory.select(Projections.constructor(
                StatsGetResponseDto.class,
                task.count(),
                completed,
                inProgress,
                todo,
                overdue,
                Expressions.constant(0L),
                Expressions.constant(0L)
        )).from(task).where(task.isDeleted.isFalse()).fetchOne();

        if (stats == null) {
            return stats;
        }

        return stats;
    }
}
