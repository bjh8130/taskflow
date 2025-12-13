package com.example.taskflow.common.auth.util;

public class ProgressCalculator {

    public static double calculate(long total, long completed) {
        if (total == 0) {
            return 0.0;
        }

        return Math.round(
                ((double) completed / total * 100.0) * 100.0
        ) / 100.0;
    }
}
