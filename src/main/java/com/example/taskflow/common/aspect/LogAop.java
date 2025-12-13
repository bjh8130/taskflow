package com.example.taskflow.common.aspect;

import com.example.taskflow.common.annotation.Loggable;
import com.example.taskflow.common.auth.security.SecurityUtil;
import com.example.taskflow.domain.activityLog.enums.LogTypes;
import com.example.taskflow.domain.activityLog.service.ActivityLogService;
import com.example.taskflow.domain.task.dto.response.TaskResponseDto;
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

            String before = null;
            if (type.equals(LogTypes.TASK_STATUS_CHANGED)) {
                Long id = (Long) args[0];
                Task task = taskRepository.findById(id).orElse(null);
                before = task.getStatus();
            }

            // 실제 메서드 실행
            result = joinPoint.proceed();

            // 메서드 실행 후
            /// 타입에 따라 로그 저장 방식 지정
            switch (type) {
                case TASK_CREATED -> {
                    TaskResponseDto dto = (TaskResponseDto) result;
                    String description = String.format("새 작업 \"%s\"을(를) 생성했습니다.", dto.getTitle());
                    activityLogService.createTaskLog(userId, type, description, result);
                }
                case TASK_UPDATED -> {
                    TaskResponseDto dto = (TaskResponseDto) result;
                    String description = String.format("작업 \"%s\" 정보를 수정했습니다.", dto.getTitle());
                    activityLogService.createTaskLog(userId, type, description, result);
                }
                case TASK_DELETED -> {
                    Long taskId = (Long) args[0];
                    Task task = taskRepository.findById(taskId).orElse(null);
                    String description = String.format("작업 \"%s\"을(를) 삭제했습니다.", task.getTitle());
                    activityLogService.createTaskDeleteLog(userId, type, description, taskId);
                }
                case TASK_STATUS_CHANGED -> {
                    TaskResponseDto after = (TaskResponseDto) result;
                    String description = String.format("작업 상태를 %s에서 %s으로 변경했습니다.", before, after.getStatus());
                    activityLogService.createTaskLog(userId, type, description, result);
                }
                case COMMENT_CREATED -> {
                    Long taskId = (Long) args[1];
                    Task task = taskRepository.findById(taskId).orElse(null);
                    String description =  String.format("작업 \"%s\"에 댓글을 작성했습니다.", task.getTitle());
                    activityLogService.createCommentCreateLog(userId, type, description, result);
                }
                case COMMENT_UPDATED -> {
                    String description = "댓글을 수정했습니다.";
                    activityLogService.createCommentUpdateLog(userId, type, description, result);
                }
                case COMMENT_DELETED -> {
                    Long taskId = (Long) args[0];
                    String description = "댓글을 삭제했습니다.";
                    activityLogService.createCommentDeleteLog(userId, type, description, taskId);
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
