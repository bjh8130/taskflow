package com.example.taskflow.domain.task.enums;


import java.util.Arrays;


public enum TaskStatus {
    TODO,
    IN_PROGRESS,
    DONE;

    public static boolean isValid(String value) {
        return Arrays.stream(values())
                .anyMatch(v -> v.name().equalsIgnoreCase(value));
    }
}
