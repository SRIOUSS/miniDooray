package com.nhnacademy.minidooraytask.member.service;

import com.nhnacademy.minidooraytask.member.domain.*;
import com.nhnacademy.minidooraytask.project.domain.Project;
import com.nhnacademy.minidooraytask.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class ProjectMemberFacade {

    private final ProjectService projectService;
    private final ProjectMemberService projectMemberService;

    @Transactional(readOnly = true)
    public MemberInfoListDto getMemberInfoList(long projectId, long accountId) {
        // projectId and accountId로 조회시 있는지 확인 없으면 오류
        projectMemberService.checkProjectMember(projectId, accountId);

        Map<String, ProjectMember> memberMap = projectMemberService.getMemberMapWithUserIdByProjectId(projectId);

        List<MemberInfoDto> memberInfoDtoList = new ArrayList<>();
        for(String userId : memberMap.keySet().stream().toList()) {
            ProjectMember pm = memberMap.get(userId);
            memberInfoDtoList.add(new MemberInfoDto(
                    pm.getAccountId(), pm.getId(), userId, pm.getAuth(), pm.getJoinedAt()
            ));
        }

        return new MemberInfoListDto(memberInfoDtoList);
    }

    @Transactional
    public void addProjectMember(Long projectId, Long accountId, MemberRequestDto memberRequestDto) {

        //projectId and accountId로 조회시 있는지
        projectMemberService.checkProjectMember(projectId, accountId);
        //이미 프로젝트에 포함된 멤버인지
        projectMemberService.checkIncludedMember(projectId, accountId);
        //멤버 권한
        projectMemberService.checkAdminAuth(memberRequestDto);

        Project project = projectService.exGetProjectById(projectId);
        projectMemberService.addProjectMember(project, memberRequestDto);

    }

    @Transactional
    public void updateMember(long projectId, long memberId, long accountId, MemberRequestDto requestDto) {
        projectMemberService.checkProjectMemberWithAuth(projectId, accountId, MembersAuth.ADMIN);

        projectMemberService.updateProjectMember(memberId, requestDto);
    }

    @Transactional
    public void deleteMember(long projectId, long memberId, long accountId) {
        if(projectMemberService.isMemberIdEqualAccountId(memberId, accountId)) {
            projectMemberService.checkProjectMember(projectId, memberId, accountId);
        } else {
            projectMemberService.checkProjectMemberWithAuth(projectId, accountId, MembersAuth.ADMIN);
        }

        projectMemberService.deleteProjectMember(projectId, memberId);
    }

}
