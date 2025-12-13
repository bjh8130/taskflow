package com.example.taskflow.common.annotation;

import com.example.taskflow.domain.activityLog.enums.LogTypes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Loggable {
    LogTypes type();
}
