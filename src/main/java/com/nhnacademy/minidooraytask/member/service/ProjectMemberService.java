package com.nhnacademy.minidooraytask.member.service;

import com.nhnacademy.minidooraytask.client.AccountApiClient;
import com.nhnacademy.minidooraytask.client.dto.account.AccountResp;
import com.nhnacademy.minidooraytask.member.domain.*;
import com.nhnacademy.minidooraytask.member.exception.AlreadyProjectMemberExistException;
import com.nhnacademy.minidooraytask.member.exception.ProjectMemberIsNotExistException;
import com.nhnacademy.minidooraytask.member.repository.ProjectMemberRepository;
import com.nhnacademy.minidooraytask.project.domain.Project;
import com.nhnacademy.minidooraytask.project.respository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectMemberService {

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;

    private final AccountApiClient client;


    //프로젝트의 멤버 유무 확인
    @Transactional(readOnly = true)
    public void checkProjectMember(long projectId, long accountId) {

        //특정 멤버 조회
        if (!projectMemberRepository.existsByProject_IdAndAccountId(projectId, accountId)) {
            log.debug("[project-member service] 존재하지 않은 멤버 조회입니다 - projectId:{}, accountId:{}", projectId, accountId);
            throw new ProjectMemberIsNotExistException("[project-member service] 존재하지 않은 멤버 조회입니다");
        }
    }

    @Transactional(readOnly = true)
    public void checkProjectMember(long projectId, long memberId, long accountId) {
        if(!projectMemberRepository.existsByProject_IdAndIdAndAccountId(projectId, memberId, accountId)) {
            log.debug("[project-member service] 존재하지 않은 멤버 조회입니다 - projectId:{}, memberId{}, accountId:{}", projectId, memberId, accountId);
            throw new ProjectMemberIsNotExistException("[project-member service] 존재하지 않은 멤버 조회입니다");
        }
    }

    @Transactional(readOnly = true)
    public ProjectMember getActiveMember(Long projectId, Long accountId) {

        return projectMemberRepository.findByProject_IdAndAccountId(projectId, accountId)
                .filter(member -> !member.isDeleted())
                .orElseThrow(() -> {
                    log.debug("[project-member service] 권한이 없는 멤버입니다 - projectId:{}, accountId:{}", projectId, accountId);
                    return new ProjectMemberIsNotExistException("[project-member service] 권한이 없는 멤버입니다");
                });
    }

    // 멤버 권한 확인
    @Transactional(readOnly = true)
    public void checkAdminAuth(MemberRequestDto memberRequestDto) {
        if (!(memberRequestDto.auth().equals(MembersAuth.ADMIN))) {
            log.debug("[project-member service] 프로젝트에 대한 권한이 존제하지 않습니다 - MemberAuth : {}", memberRequestDto.auth());
            throw new IllegalArgumentException("[project-member service] 프로젝트에 대한 권한이 존제하지 않습니다");
        }
    }

    @Transactional(readOnly = true)
    public void checkProjectMemberWithAuth(long projectId, long accountId, MembersAuth auth) {
        if(!projectMemberRepository.existsByProject_IdAndAccountIdAndAuth(projectId, accountId, auth)) {
            log.debug("[project-member service] 존재하지 않은 멤버 조회입니다 - projectId:{}, accountId:{}, auth:{}", projectId, accountId, auth);
            throw new ProjectMemberIsNotExistException("[project-member service] 존재하지 않은 멤버 조회입니다");
        }
    }

    @Transactional(readOnly = true)
    public void checkIncludedMember(Long projectId, Long accountId) {
        if(projectMemberRepository.findByProject_IdAndAccountId(projectId, accountId).isEmpty()) {
            log.debug("[project-member service] 프로젝트에 포함된 멤버가 아닙니다 - projectId:{}, accountId:{}", projectId, accountId);
            throw new ProjectMemberIsNotExistException("[project-member service] 프로젝트에 포함된 멤버가 아닙니다");
        }
    }

    @Transactional
    public ProjectMember getMemberByTaskId(long taskId, long accountId) {
        return projectMemberRepository.findProjectMemberByTaskIdAndAccountId(taskId, accountId);
    }

    @Transactional(readOnly = true)
    public boolean isMemberIdEqualAccountId(long memberId, long accountId) {
        return projectMemberRepository.existsProjectMemberByIdAndAccountId(memberId, accountId);
    }


    @Transactional(readOnly = true)
    public Long getProjectMemberIdByUserId(String userId) {
        AccountResp accountResp = client.getAccountByUserId(userId);

        return Objects.nonNull(accountResp) ? accountResp.id() : null;
    }

    @Transactional(readOnly = true)
    public String getUserIdByAccountId(long accountId) {
        AccountResp account = client.getAccountById(accountId);
        return account.userId();
    }

    @Transactional(readOnly = true)
    public Map<Long, String> getUserIdsByMemberId(List<Long> memberIds) {
        List<ProjectMember> projectMemberList = projectMemberRepository.findProjectMembersByIdIn(memberIds);

        Map<Long, AccountResp> accountRespMap = getAccountRespMap(projectMemberList);

        Map<Long, String> memberIdAndUserId = new HashMap<>();
        for(Long mi : accountRespMap.keySet().stream().toList()) {
            AccountResp accountResp = accountRespMap.get(mi);
            memberIdAndUserId.put(mi, accountResp.userId());
        }

        return memberIdAndUserId;
    }

    private Map<Long, AccountResp> getAccountRespMap(List<ProjectMember> projectMemberList) {
        Map<Long, Long> memberIdMap = projectMemberList.stream().collect(Collectors.toMap(ProjectMember::getId, ProjectMember::getAccountId));
        Map<Long, AccountResp> accountByIds = client.getAccountByIds(memberIdMap.values().stream().toList());

        Map<Long, AccountResp> accountRespMap = new HashMap<>();
        for(Long mi: memberIdMap.keySet().stream().toList()) {
            Long accountId = memberIdMap.get(mi);
            accountRespMap.put(mi, accountByIds.get(accountId));
        }

        return accountRespMap;
    }

    @Transactional(readOnly = true)
    public Map<String, ProjectMember> getMemberMapWithUserIdByMemberId(List<Long> memberIds) {
        List<ProjectMember> members = projectMemberRepository.findProjectMembersByIdIn(memberIds);

        return createMemberMapWithUserId(members);
    }

    @Transactional(readOnly = true)
    public Map<String, ProjectMember> getMemberMapWithUserIdByProjectId(long projectId) {
        List<ProjectMember> projectMembers = projectMemberRepository.findProjectMembersByProject_Id(projectId);

        return createMemberMapWithUserId(projectMembers);
    }

    private Map<String, ProjectMember> createMemberMapWithUserId(List<ProjectMember> members) {
        Map<Long, AccountResp> accountRespMap = getAccountRespMap(members);
        Map<Long, ProjectMember> memberMap = members.stream().collect(Collectors.toMap(ProjectMember::getId, m -> m));

        Map<String, ProjectMember> projectMemberMap = new HashMap<>();
        for(Long mi : accountRespMap.keySet().stream().toList()) {
            String userId = accountRespMap.get(mi).userId();
            ProjectMember projectMember = memberMap.get(mi);
            projectMemberMap.put(userId, projectMember);
        }

        return projectMemberMap;
    }


    //멤버 추가
    @Transactional
    public void addProjectMember(Project project,MemberRequestDto memberRequestDto) {

        // 추가하려는 대상 아이디
        Long targetAccountId = memberRequestDto.accountId();

        ProjectMember targetMember = projectMemberRepository.findByProject_IdAndAccountId(project.getId(), targetAccountId)
                .map(existingMember -> {
                    // 멤버가 존재하는데, 삭제된 상태라면 복구
                    if (existingMember.isDeleted()) {
                        existingMember.restore();
                        log.debug("삭제된 멤버 재활성화 - memberId: {}", existingMember.getId());
                        return existingMember;
                    } else {
                        //삭제되지 않은 멤버라면 예외
                        throw new AlreadyProjectMemberExistException("[project-member service] 이미 존재하는 멤버 입니다");
                    }
                })
                //아예 존재하지 않는다면
                .orElseGet(() -> {
                    ProjectMember newMember = new ProjectMember(project, targetAccountId, memberRequestDto.auth());
                    return projectMemberRepository.save(newMember);
                });


    }

    @Transactional
    public void updateProjectMember(long memberId, MemberRequestDto requestDto) {
        ProjectMember projectMember = projectMemberRepository.findProjectMemberById(memberId);

        if(!requestDto.auth().equals(projectMember.getAuth())) {
            projectMember.setAuth(requestDto.auth());
        }

        projectMemberRepository.save(projectMember);
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

    @Transactional(readOnly = true)
    public List<ProjectMember> getProjectMemberByAccountId(long accountId) {
        return projectMemberRepository.findAllById(accountId);
    }

    @Transactional(readOnly = true)
    public ProjectMember getProjectMemberByProjectIdAndAccountId(long projectId, long accountId) {
        return projectMemberRepository.findByProject_IdAndAccountId(projectId, accountId).orElseGet(null);
    }
}
