package com.example.taskflow.domain.task.repository;

import com.example.taskflow.domain.task.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task,Long> {
    Page<Task> findAllByIsDeletedFalse(Pageable pageable);
    Page<Task> findAllByStatusAndIsDeletedFalse(String status, Pageable pageable);
    Optional<Task> findByIdAndIsDeletedFalse(Long id);

    long countByUserIdAndStatusAndIsDeletedFalse(Long userId, String status);

    long countByUserIdAndIsDeletedFalse(long userId);

    List<Task> findAllByUserIdAndIsDeletedFalseAndDueDateGreaterThanEqualAndDueDateLessThan(long userId, LocalDateTime start, LocalDateTime end);

    List<Task> findAllByUserIdAndIsDeletedFalseAndDueDateGreaterThanEqual(Long userId, LocalDateTime end);

    List<Task> findAllByUserIdAndIsDeletedFalseAndDueDateLessThanAndStatusNot(long userId, LocalDateTime start, String status);
}
