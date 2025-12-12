package com.example.taskflow.common.auth.security;

import com.example.taskflow.common.exception.CustomException;
import com.example.taskflow.common.exception.ErrorCode;
import com.example.taskflow.domain.user.entity.User;
import com.example.taskflow.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Spring Security의 UserDetailsService 구현체.
 * - Security가 로그인 또는 인증 과정에서 사용자 정보를 조회할 때 사용
 * - JWT의 subject(userId)를 기반으로 DB에서 User 엔티티를 조회,
 *   조회된 User를 PrincipalDetails로 감싸서 SecurityContext에 올릴 수 있도록 반환
 */
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * 로그인 시 호출되는 메서드.
     *
     * - Spring Security는 "username"이라는 이름을 사용하지만,
     *   JWT의 subject 값(= userId)을 username처럼 사용한다.
     * - 전달된 userId로 DB에서 User 엔티티를 조회하고,
     *   인증 객체로 쓰기 위해 PrincipalDetails로 포장해 반환한다.
     *
     * @param userId  JWT에서 추출한 userId (문자열 형태)
     * @return PrincipalDetails  Security가 인증 정보로 사용할 객체
     * @throws UsernameNotFoundException  userId에 해당하는 사용자가 없을 경우 발생
     */
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return new PrincipalDetails(user);
    }
}
