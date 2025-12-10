package com.example.taskflow.domain.team.service;

import com.example.taskflow.common.exception.CustomException;
import com.example.taskflow.common.exception.ErrorCode;
import com.example.taskflow.domain.team.dto.request.TeamCreateRequestDto;
import com.example.taskflow.domain.team.dto.response.TeamCreateResponseDto;
import com.example.taskflow.domain.team.entity.Team;
import com.example.taskflow.domain.teamMember.repository.TeamMemberRepository;
import com.example.taskflow.domain.team.repository.TeamRepository;
import com.example.taskflow.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;

    @Transactional
    public TeamCreateResponseDto save(TeamCreateRequestDto request) {
        if (teamRepository.existsByName(request.getName())) {
            throw new CustomException(ErrorCode.TEAMNAME_ALREADY_EXISTS);
        }

        Team team = new Team(request.getName(), request.getDescription());
        teamRepository.save(team);

        List<User> members = teamMemberRepository.findUserIdByTeamId(team.getId());

        return new TeamCreateResponseDto(team.getId(), team.getName(), team.getDescription(), team.getCreatedAt(), members);
    }
}
