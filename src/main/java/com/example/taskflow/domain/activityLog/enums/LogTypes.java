package com.example.taskflow.domain.activityLog.enums;

import lombok.Getter;

@Getter
public enum LogTypes {
    TASK_CREATED("작업 생성", "작업이 생성되었습니다."),
    TASK_UPDATED("작업 수정", "작업이 수정되었습니다."),
    TASK_DELETED("작업 삭제", "작업이 삭제되었습니다."),
    TASK_STATUS_CHANGED("작업 상태 변경", "작업 상태가 변경되었습니다."),
    COMMENT_CREATED("댓글 작성", "댓글이 생성되었습니다."),
    COMMENT_UPDATED("댓글 수정", "댓글이 수정되었습니다."),
    COMMENT_DELETED("댓글 삭제", "댓글이 삭제되었습니다.");

    public final String name;
    public final String description;

    LogTypes(String name, String description) {
        this.name = name;
        this.description = description;
    }

}
