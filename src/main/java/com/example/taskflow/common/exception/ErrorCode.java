package com.example.taskflow.common.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    //------409-----------------------
    USER_ALREADY_EXISTS(409, "이미 존재하는 사용자 이메일입니다."),
    CELLPHONENUMBER_ALREADY_EXISTS(409, "이미 존재하는 전화번호입니다."),
    ALREADY_LIKED(409, "이미 좋아요 되어있습니다."),
    USERNAME_ALREADY_EXISTS(409, "이미 존재하는 사용자 아이디입니다."),
    //------404-----------------------
    USER_NOT_FOUND(404, "없는 유저입니다"),
    TASK_NOT_FOUND(404, "존재하지 않는 작업입니다."),

    //------403-----------------------
    USER_NOT_MATCH(403, "접근 권한이 없습니다"),
    PASSWORD_NOT_MATCH(403, "비밀번호가 일치하지 않습니다."),
    FORBIDDEN(403, "접근 권한이 없습니다"),
    //------401-----------------------
    LOGIN_REQUIRED(401, "로그인한 유저만 사용할 수 있는 기능입니다"),
    //------400-----------------------
    INVALID_EMAIL_FORMAT(400, "이메일 형식이 올바르지 않습니다."),
    INVALID_PASSWORD_FORMAT(400, "비밀번호 형식이 올바르지 않습니다."),
    INVALID_PASSWORD(400, "비밀번호가 유효하지 않습니다."),
    VALIDATION_ERROR(400, "입력값이 유효하지 않습니다.");

    private final int status;
    private final String message;

    ErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
