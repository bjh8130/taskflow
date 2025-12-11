package com.example.taskflow.domain.user.repository;

import com.example.taskflow.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, long userId);

    Optional<User> findByIdAndIsDeletedFalse(long userId);

    Optional<User> findAllByIsDeletedFalse();

    @Query("""
    SELECT u FROM User u
    WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :query, '%'))
       OR LOWER(u.name) LIKE LOWER(CONCAT('%', :query, '%'))
    """)
    List<User> searchUsers(String query);

    @Query("""
    SELECT u FROM User u
    WHERE u.isDeleted = false
    AND u.id NOT IN (
        SELECT tm.user.id FROM TeamMember tm WHERE tm.team.id = :teamId)
    """)
    List<User> findAllAvailableUsers(long teamId);
}
