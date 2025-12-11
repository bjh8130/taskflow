package com.example.taskflow.domain.teamMember.repository;

import com.example.taskflow.domain.teamMember.entity.TeamMember;
import com.example.taskflow.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {

    @Query("SELECT t.user FROM TeamMember t WHERE t.team.id = :teamId")
    List<User> findUserByTeamId(@Param("teamId") Long teamId);

    boolean existsByTeamId(Long teamId);

    boolean existsByTeamIdAndUserId(Long teamId, Long userId);
}