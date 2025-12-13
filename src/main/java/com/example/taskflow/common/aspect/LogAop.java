package com.example.taskflow.common.aspect;

import com.example.taskflow.common.annotation.Loggable;
import com.example.taskflow.common.auth.security.SecurityUtil;
import com.example.taskflow.domain.activityLog.enums.LogTypes;
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
        /// 임의 반환을 위한 Object 선언
        Object result = null;


        try {
            /// 메서드 인자 받아두기
            Object[] args = joinPoint.getArgs();

            /// 접근 유저 정보 받아오기
            Long userId = SecurityUtil.getCurrentUserId();

            /// 어노테이션 정보 미리 받아오기
            LogTypes type = loggable.type();

            // 실제 메서드 실행
            result = joinPoint.proceed();

            // 메서드 실행 후
            /// 타입에 따라 로그 저장 방식 지정
            switch (type) {
                case TASK_CREATED, TASK_UPDATED -> {
                    activityLogService.createTaskLog(userId, type, result);
                }
                case TASK_DELETED -> {
                    Long taskId = (Long) args[0];
                    activityLogService.createTaskDeleteLog(userId, type, taskId);
                }
                case COMMENT_CREATED -> {
                    activityLogService.createCommentLog(userId, type, result);
                }
                case COMMENT_UPDATED -> {
                    activityLogService.createCommentUpdateLog(userId, type, result);
                }
                case COMMENT_DELETED -> {
                    Long taskId = (Long) args[0];
                    activityLogService.createCommentDeleteLog(userId, type, taskId);
                }
                default -> {
                }
            }

            return result;

        } catch (Exception e) {

            return result;

        }
    }
}
