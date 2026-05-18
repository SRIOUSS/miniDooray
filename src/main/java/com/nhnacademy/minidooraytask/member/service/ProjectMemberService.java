package com.nhnacademy.minidooraytask.member.service;

import com.nhnacademy.minidooraytask.member.domain.ProjectMember;
import com.nhnacademy.minidooraytask.member.domain.ProjectMemberAddRequestDto;
import com.nhnacademy.minidooraytask.member.domain.ProjectMemberResponseDto;
import com.nhnacademy.minidooraytask.member.exception.AlreadyProjectMemberExistException;
import com.nhnacademy.minidooraytask.member.exception.ProjectMemberIsNotExistException;
import com.nhnacademy.minidooraytask.member.repository.ProjectMemberRepository;
import com.nhnacademy.minidooraytask.project.domain.Project;
import com.nhnacademy.minidooraytask.project.exception.ProjectNotFoundException;
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
    @Transactional(readOnly = true)
    public List<ProjectMemberResponseDto> getProjectMembers(Long projectId) {

        return projectMemberRepository.findAllByProjectId(projectId)
                .stream()
                .map(ProjectMemberResponseDto::from)
                .toList();
    }

    //멤버 추가
    @Transactional
    public void addProjectMember(Long projectId, ProjectMemberAddRequestDto request) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("[project-member service] 존재하지 않는 프로젝트입니다"));

        projectMemberRepository.findByProject_IdAndAccountId(projectId, request.accountId())
                .ifPresentOrElse(
                        existingMember -> {
                            // 멤버가 존재하는데, 삭제된 상태라면 복구
                            if (existingMember.isDeleted()) {
                                existingMember.restore();
                                log.debug("삭제된 멤버 재활성화 - memberId: {}", existingMember.getId());
                            } else {
                                //삭제되지 않은 멤버라면 예외
                                throw new AlreadyProjectMemberExistException("[project-member service] 이미 존재하는 멤버 입니다");
                            }
                        },
                        () -> {
                            //아예 존재하지 않으면 새로 생성해서 저장
                            ProjectMember newMember = new ProjectMember(project, request.accountId());
                            projectMemberRepository.save(newMember);
                        }
                );
    }

    //삭제
    @Transactional
    public void deleteProjectMember(Long projectId, Long memberId) { // 파라미터에 projectId 추가
        ProjectMember member = projectMemberRepository.findById(memberId)
                .orElseThrow(() -> new ProjectMemberIsNotExistException("[project-member service] 존재하지 않는 멤버입니다"));

        // 요청된 projectId와 이 멤버가 속한 projectId가 같은지 검증!
        if (!member.getProject().getId().equals(projectId)) {
            throw new IllegalArgumentException("해당 프로젝트의 멤버가 아닙니다.");
        }

        member.delete();
    }
}
