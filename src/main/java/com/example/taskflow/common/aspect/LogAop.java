package com.example.taskflow.common.aspect;

import com.example.taskflow.common.annotation.Loggable;
import com.example.taskflow.domain.activityLog.service.ActivityLogService;
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

    // 어떤 것을 :
    // - Task : 생성, 수정, 삭제, 상태 변경
    // - Comment : 작성, 수정, 삭제
    // - 사용자 : 로그인, 로그아웃

    // 언제 : 메서드 실행 후

    // 어떻게

    // 메서드 실행 시 로그를 남기는 기능

    @Around("@annotation(loggable)")
    public Object recordLog(ProceedingJoinPoint joinPoint, Loggable loggable) throws Throwable {

        // 메서드 실행 전
        /// 콘솔 로그용 실행 시간 측정

        /// 어노테이션 정보 미리 받아오기
        String type = loggable.type().name();
        String description = loggable.type().getDescription();

        // 실제 메서드 실행
        Object result = joinPoint.proceed();
        Object[] args = joinPoint.getArgs();

        // 메서드 실행 후
        /// 타입에 따라 로그 저장 방식 지정
        switch (type) {
            case "TASK_CREATED" -> {
                Long userId = (Long) args[1];
                activityLogService.createTaskLog(userId, type, description, result);
            }
            case "TASK_UPDATED" -> {
                Long userId = (Long) args[0];
                activityLogService.createTaskLog(userId, type, description, result);
            }
            case "TASK_DELETED" -> {
                Long userId = (Long) args[0];
                Long taskId = (Long) args[0];
                activityLogService.createTaskDeleteLog(userId, type, description, taskId);
            }
            case "COMMENT_CREATED" -> {
                Long userId = (Long) args[0];
                activityLogService.createCommentLog(userId, type, description, result);
            }
            case "COMMENT_UPDATED" -> {
                Long userId = (Long) args[0];
                activityLogService.createCommentUpdateLog(userId, type, description, result);
            }
            case "COMMENT_DELETED" -> {
                Long userId = (Long) args[0];
                Long taskId = (Long) args[0];
                activityLogService.createCommentDeleteLog(userId, type, description, taskId);
            }
            default -> {}
        }

        return result;
    }
}
