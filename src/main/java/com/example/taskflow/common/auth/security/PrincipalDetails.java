package com.example.taskflow.common.auth.security;

import com.example.taskflow.domain.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class PrincipalDetails implements UserDetails {

    private final User user;

    //사용자의 권한 목록 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }
    // Spring Security가 로그인 시 비밀번호 검증할 때 사용하는 값
    // DB에 저장된 암호화된 비밀번호를 그대로 반환
    @Override public String getPassword() { return user.getPassword(); }
    // 로그인 시 사용되는 식별자
    // 프로젝트 정책에 따라 username 또는 email을 반환하도록 변경 가능
    @Override public String getUsername() { return user.getUsername(); }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
