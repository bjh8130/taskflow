package com.example.taskflow.domain.user.repository;

import com.example.taskflow.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, long userId);

    Optional<User> findByIdAndIsDeletedFalse(long userId);

    List<User> findAllByIsDeletedFalse();

    @Query("""
    SELECT u FROM User u
    WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :query, '%'))
       OR LOWER(u.name) LIKE LOWER(CONCAT('%', :query, '%'))
    """)
    List<User> searchUsers(String query);

    List<User> findByName(String name);

    Optional<User> findByUsername(String username);

    @Query("""
    SELECT u FROM User u
    WHERE u.isDeleted = false
      AND NOT EXISTS (
          SELECT tm.id
          FROM TeamMember tm
          WHERE tm.user = u
      )
    """)
    List<User> findAllAvailableUsers(@Param("teamId") Long teamId);
}
