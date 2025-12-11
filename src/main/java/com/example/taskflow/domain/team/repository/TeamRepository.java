package com.example.taskflow.domain.team.repository;

import com.example.taskflow.domain.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {

    boolean existsByName(String name);

    @Query("""
    SELECT tm FROM Team tm
    WHERE LOWER(tm.name) LIKE LOWER(CONCAT('%', :query, '%'))
       OR LOWER(tm.description) LIKE LOWER(CONCAT('%', :query, '%'))
    """)
    List<Team> searchTeams(String query);
}
