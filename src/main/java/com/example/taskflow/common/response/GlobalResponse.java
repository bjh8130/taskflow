package com.example.taskflow.common.response;

import com.example.taskflow.common.exception.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GlobalResponse<T> {
    private boolean success;
    private String message;
    private T data;

    //성공시
    public static <T> GlobalResponse<T> success(boolean success, String message, T data) {
        return new GlobalResponse<>(success, message, data); //204 는 data null로 넣어주세요.
    }
    //예외처리시
    public static GlobalResponse<Void> exception(boolean success,ErrorCode errorCode) {
        return new GlobalResponse<>(success, errorCode.getMessage(), null);
    }
}