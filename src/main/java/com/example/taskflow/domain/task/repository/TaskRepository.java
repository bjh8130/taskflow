package com.example.taskflow.domain.task.repository;

import com.example.taskflow.domain.task.entity.Task;
import com.example.taskflow.domain.task.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Task 엔티티에 대한 데이터베이스 접근을 담당하는 Repository입니다.
 * 기본 CRUD 외에도 상태, 조건 기반 조회 등 Task 검색을 위한 메서드를 제공합니다.
 */
public interface TaskRepository extends JpaRepository<Task,Long> {
    Page<Task> findAllByIsDeletedFalse(Pageable pageable);
    Page<Task> findAllByStatusAndIsDeletedFalse(TaskStatus status, Pageable pageable);
    Optional<Task> findByIdAndIsDeletedFalse(Long id);

    long countByUserIdAndStatusAndIsDeletedFalse(Long userId, TaskStatus status);

    long countByUserIdAndIsDeletedFalse(long userId);

    List<Task> findAllByUserIdAndIsDeletedFalseAndDueDateGreaterThanEqualAndDueDateLessThan(long userId, LocalDateTime start, LocalDateTime end);

    List<Task> findAllByUserIdAndIsDeletedFalseAndDueDateGreaterThanEqual(Long userId, LocalDateTime end);

    List<Task> findAllByUserIdAndIsDeletedFalseAndDueDateLessThanAndStatusNot(long userId, LocalDateTime start, TaskStatus status);

    @Query("""
    SELECT t FROM Task t
    WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', :query, '%'))
       OR LOWER(t.description) LIKE LOWER(CONCAT('%', :query, '%'))
    """)
    List<Task> searchTasks(String query);

    List<Task> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    List<Task> findByDueDateBetweenAndIsDeletedFalse(LocalDateTime start, LocalDateTime end);

    List<Task> findByCreatedAtLessThanEqualAndIsDeletedFalse(LocalDateTime date);

    List<Task> findByCompletedDateBetweenAndIsDeletedFalse(LocalDateTime start, LocalDateTime end);

    @Query("""
        select count(t)
        from Task t
        where t.isDeleted = false
          and t.user.id in (
              select tm.user.id
              from TeamMember tm
              where tm.team.id = :teamId
          )
    """)
    long countTeamTotal(@Param("teamId") Long teamId);

    @Query("""
        select count(t)
        from Task t
        where t.isDeleted = false
          and t.status = 'DONE'
          and t.user.id in (
              select tm.user.id
              from TeamMember tm
              where tm.team.id = :teamId
          )
    """)
    long countTeamCompleted(@Param("teamId") Long teamId);
}