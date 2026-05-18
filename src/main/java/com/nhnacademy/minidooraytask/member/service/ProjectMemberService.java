package com.nhnacademy.minidooraytask.member.service;

import com.nhnacademy.minidooraytask.member.domain.ProjectMember;
import com.nhnacademy.minidooraytask.member.domain.ProjectMemberAddRequestDto;
import com.nhnacademy.minidooraytask.member.domain.ProjectMemberResponseDto;
import com.nhnacademy.minidooraytask.member.exception.AlreadyProjectMemberExistException;
import com.nhnacademy.minidooraytask.member.exception.ProjectMemberIsNotExistException;
import com.nhnacademy.minidooraytask.member.repository.ProjectMemberRepository;
import com.nhnacademy.minidooraytask.project.domain.Project;
import com.nhnacademy.minidooraytask.project.respository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectMemberService {

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;

    //프로젝트의 멤버 유무 확인
    @Transactional(readOnly = true)
    public void existProjectMemberByProjectIdAndAccountId(long projectId, long accountId) {
        if(!projectMemberRepository.existsProjectMemberByProject_IdAndAccountId(projectId, accountId)) {
            log.debug("[project-member service] 존재하지 않은 멤버 조회입니다 - projectId:{}, accountId:{}", projectId, accountId);
            throw new ProjectMemberIsNotExistException("[project-member service] 존재하지 않은 멤버 조회입니다");
        }
    }

    //프로젝트의 멤버조회
    public List<ProjectMemberResponseDto> getProjectMembers(Long projectId) {

        return projectMemberRepository.findAllByProjectId(projectId)
                .stream()
                .map(ProjectMemberResponseDto::from)
                .toList();
    }

    //멤버 추가
    @Transactional
    public void addProjectMember(Long projectId, ProjectMemberAddRequestDto request) {

        Project project = projectRepository.findByProjectId(projectId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 프로젝트입니다. projectId: " + projectId));

        // 이미 존재하는 멤버인지 확인
        if(projectMemberRepository.existsProjectMemberByProject_IdAndAccountId(projectId, request.getAccountId())) {
            log.debug("[project-member service] 이미 존재하는 멤버입니다 - projectId: {}, accountId: {}", projectId, request.getAccountId());
            throw new AlreadyProjectMemberExistException("[project-member service] 이미 존재하는 멤버 입니다");
        }

        ProjectMember member = new ProjectMember(project, request.getAccountId());
        projectMemberRepository.save(member);
    }

    //삭제
    @Transactional
    public void deleProjectMember(Long memberId) {
        ProjectMember member = projectMemberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 멤버 아이디 입니다. : " + memberId));

        member.delete();
    }
}
