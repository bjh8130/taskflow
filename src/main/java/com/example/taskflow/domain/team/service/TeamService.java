package com.example.taskflow.domain.team.service;

import com.example.taskflow.common.exception.CustomException;
import com.example.taskflow.common.exception.ErrorCode;
import com.example.taskflow.domain.team.dto.request.TeamCreateRequestDto;
import com.example.taskflow.domain.team.dto.request.TeamUpdateRequestDto;
import com.example.taskflow.domain.team.dto.response.TeamCreateResponseDto;
import com.example.taskflow.domain.team.dto.response.TeamReadResponseDto;
import com.example.taskflow.domain.team.dto.response.TeamUpdateResponseDto;
import com.example.taskflow.domain.team.entity.Team;
import com.example.taskflow.domain.teamMember.repository.TeamMemberRepository;
import com.example.taskflow.domain.team.repository.TeamRepository;
import com.example.taskflow.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    @Transactional(readOnly = true)
    public List<TeamReadResponseDto> findAll() {
        List<Team> teams = teamRepository.findAll();
        List<TeamReadResponseDto> dtos = new ArrayList<>();

        for (Team team : teams) {
            List<User> members = teamMemberRepository.findUserIdByTeamId(team.getId());
            dtos.add(new TeamReadResponseDto(team.getId(), team.getName(), team.getDescription(), team.getCreatedAt(), members));
        }

        return dtos;
    }

    @Transactional(readOnly = true)
    public TeamReadResponseDto findOne(Long id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));

        List<User> members = teamMemberRepository.findUserIdByTeamId(team.getId());

        return new TeamReadResponseDto(team.getId(), team.getName(), team.getDescription(), team.getCreatedAt(), members);
    }

    @Transactional
    public TeamUpdateResponseDto update(Long id, TeamUpdateRequestDto request) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));

        List<User> members = teamMemberRepository.findUserIdByTeamId(team.getId());

        if (request.getName() != null) {
            team.updateName(request.getName());
        }

        if (request.getDescription() != null) {
            team.updateDescription(request.getDescription());
        }

        return new TeamUpdateResponseDto(team.getId(), team.getName(), team.getDescription(), team.getCreatedAt(), members);
    }

    @Transactional
    public void delete(Long id) {
        if (!teamRepository.existsById(id)) {
            throw new CustomException(ErrorCode.TEAM_NOT_FOUND);
        }

        if (teamMemberRepository.existsByTeamId(id)) {
            throw new CustomException(ErrorCode.TEAM_NOT_EMPTY);
        }

        teamRepository.deleteById(id);
    }
}
