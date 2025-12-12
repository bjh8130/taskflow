package com.example.taskflow.common.aspect;

import com.example.taskflow.common.annotation.ActivityLog;
import com.example.taskflow.domain.activityLog.service.ActivityLogService;
import com.example.taskflow.domain.comment.entity.Comment;
import com.example.taskflow.domain.comment.repository.CommentRepository;
import com.example.taskflow.domain.task.entity.Task;
import com.example.taskflow.domain.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class LogAop {

    private final ActivityLogService activityLogService;
    private final TaskRepository taskRepository;
    private final CommentRepository commentRepository;

    // 어떤 것을 :
    // - Task : 생성, 수정, 삭제, 상태 변경
    // - Comment : 작성, 수정, 삭제
    // - 사용자 : 로그인, 로그아웃

    // 언제 : 메서드 실행 후

    // 어떻게

    // 메서드 실행 시 로그를 남기는 기능

    @Around("@annotation(activityLog)")
    public Object recordLog(ProceedingJoinPoint joinPoint, ActivityLog activityLog) throws Throwable {

        // 어노테이션 정보 미리 받아오기
        String type = activityLog.type().name();
        String description = activityLog.type().getDescription();

        // Comment 삭제 메서드
        if (type.equals("COMMENT_DELETED")) {
            Object[] args = joinPoint.getArgs();
            Long taskId = (Long) args[0];
            Comment comment = commentRepository.findById(taskId).orElse(null);

            Object result = joinPoint.proceed(); // 실제 메서드 실행

            activityLogService.createCommentDeleteLog(type, description, comment);
            return result;
        }

        // Comment 수정 메서드
        else if (type.equals("COMMENT_UPDATED")) {
            Object result = joinPoint.proceed(); // 실제 메서드 실행
            activityLogService.createCommentUpdateLog(type, description, result);
            return result;
        }

        // Comment 작성 메서드
        else if (type.equals("COMMENT_CREATED")) {
            Object result = joinPoint.proceed(); // 실제 메서드 실행
            activityLogService.createCommentLog(type, description, result);
            return result;
        }

        // Task 삭제 메서드
        else if (type.equals("TASK_DELETED")) {
            Object[] args = joinPoint.getArgs();
            Long taskId = (Long) args[0];
            Task task = taskRepository.findById(taskId).orElse(null);

            Object result = joinPoint.proceed(); // 실제 메서드 실행

            activityLogService.createTaskDeleteLog(type, description, task);
            return result;
        }

        // Task 생성, 수정 메서드
        else {
            Object result = joinPoint.proceed(); // 실제 메서드 실행
            activityLogService.createTaskLog(type, description, result);
            return result;
        }
    }
}
