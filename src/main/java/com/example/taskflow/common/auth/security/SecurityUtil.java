package com.example.taskflow.common.auth.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtil {

    private SecurityUtil() {} // new 방지

    public static PrincipalDetails getPrincipal() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated() ||
                authentication.getPrincipal().equals("anonymousUser")) {
            return null;
        }

        return (PrincipalDetails) authentication.getPrincipal();
    }

    public static Long getCurrentUserId() {
        PrincipalDetails principal = getPrincipal();
        return principal != null ? principal.getUser().getId() : null;
    }
}