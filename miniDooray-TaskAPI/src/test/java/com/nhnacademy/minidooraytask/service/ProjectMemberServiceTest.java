package com.nhnacademy.minidooraytask.service;

import com.nhnacademy.minidooraytask.client.AccountApiClient;
import com.nhnacademy.minidooraytask.client.dto.account.AccountResp;
import com.nhnacademy.minidooraytask.member.domain.MemberRequestDto;
import com.nhnacademy.minidooraytask.member.domain.MembersAuth;
import com.nhnacademy.minidooraytask.member.domain.ProjectMember;
import com.nhnacademy.minidooraytask.member.exception.AlreadyProjectMemberExistException;
import com.nhnacademy.minidooraytask.member.exception.ProjectMemberIsNotExistException;
import com.nhnacademy.minidooraytask.member.repository.ProjectMemberRepository;
import com.nhnacademy.minidooraytask.member.service.ProjectMemberService;
import com.nhnacademy.minidooraytask.project.domain.Project;
import com.nhnacademy.minidooraytask.project.exception.NoAuthoProjectException;
import com.nhnacademy.minidooraytask.project.respository.ProjectRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class ProjectMemberServiceTest {

    @InjectMocks
    private ProjectMemberService projectMemberService;

    @Mock
    private ProjectMemberRepository projectMemberRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private AccountApiClient client;

    // ===== checkProjectMember(projectId, accountId) =====

    @Test
    @DisplayName("프로젝트 멤버 확인 - 성공")
    void checkProjectMember_success() {
        given(projectMemberRepository.existsByProject_IdAndAccountId(1L, 100L)).willReturn(true);
        projectMemberService.checkProjectMember(1L, 100L);
    }

    @Test
    @DisplayName("프로젝트 멤버 확인 - 실패 (멤버 없음)")
    void checkProjectMember_fail_notExist() {
        given(projectMemberRepository.existsByProject_IdAndAccountId(1L, 100L)).willReturn(false);
        assertThatThrownBy(() -> projectMemberService.checkProjectMember(1L, 100L))
                .isInstanceOf(ProjectMemberIsNotExistException.class);
    }

    // ===== checkProjectMember(projectId, memberId, accountId) =====

    @Test
    @DisplayName("프로젝트 멤버 확인 (memberId 포함) - 성공")
    void checkProjectMember_withMemberId_success() {
        given(projectMemberRepository.existsByProject_IdAndIdAndAccountId(1L, 10L, 100L)).willReturn(true);
        projectMemberService.checkProjectMember(1L, 10L, 100L);
    }

    @Test
    @DisplayName("프로젝트 멤버 확인 (memberId 포함) - 실패")
    void checkProjectMember_withMemberId_fail() {
        given(projectMemberRepository.existsByProject_IdAndIdAndAccountId(1L, 10L, 100L)).willReturn(false);
        assertThatThrownBy(() -> projectMemberService.checkProjectMember(1L, 10L, 100L))
                .isInstanceOf(ProjectMemberIsNotExistException.class);
    }

    // ===== getActiveMember =====

    @Test
    @DisplayName("활성 멤버 조회 - 성공")
    void getActiveMember_success() {
        ProjectMember member = new ProjectMember(new Project("t", "d", 1L), 100L, MembersAuth.MEMBER);
        given(projectMemberRepository.findProjectMemberByProject_IdAndAccountId(1L, 100L))
                .willReturn(Optional.of(member));

        ProjectMember result = projectMemberService.getActiveMember(1L, 100L);
        assertThat(result).isEqualTo(member);
    }

    @Test
    @DisplayName("활성 멤버 조회 - 실패 (삭제된 멤버)")
    void getActiveMember_fail_deleted() {
        ProjectMember member = new ProjectMember(new Project("t", "d", 1L), 100L, MembersAuth.MEMBER);
        member.delete();
        given(projectMemberRepository.findProjectMemberByProject_IdAndAccountId(1L, 100L))
                .willReturn(Optional.of(member));

        assertThatThrownBy(() -> projectMemberService.getActiveMember(1L, 100L))
                .isInstanceOf(ProjectMemberIsNotExistException.class);
    }

    @Test
    @DisplayName("활성 멤버 조회 - 실패 (멤버 없음)")
    void getActiveMember_fail_notExist() {
        given(projectMemberRepository.findProjectMemberByProject_IdAndAccountId(1L, 100L))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> projectMemberService.getActiveMember(1L, 100L))
                .isInstanceOf(ProjectMemberIsNotExistException.class);
    }

    // ===== checkAdminAuth =====

    @Test
    @DisplayName("관리자 권한 확인 - 성공")
    void checkAdminAuth_success() {
        given(projectMemberRepository.existsByProject_IdAndAccountIdAndAuth(1L, 100L, MembersAuth.ADMIN)).willReturn(true);
        projectMemberService.checkAdminAuth(1L, 100L);
    }

    @Test
    @DisplayName("관리자 권한 확인 - 실패")
    void checkAdminAuth_fail() {
        given(projectMemberRepository.existsByProject_IdAndAccountIdAndAuth(1L, 100L, MembersAuth.ADMIN)).willReturn(false);
        assertThatThrownBy(() -> projectMemberService.checkAdminAuth(1L, 100L))
                .isInstanceOf(NoAuthoProjectException.class);
    }

    // ===== checkProjectMemberWithAuth =====

    @Test
    @DisplayName("권한 포함 멤버 확인 - 성공")
    void checkProjectMemberWithAuth_success() {
        given(projectMemberRepository.existsByProject_IdAndAccountIdAndAuth(1L, 100L, MembersAuth.MEMBER)).willReturn(true);
        projectMemberService.checkProjectMemberWithAuth(1L, 100L, MembersAuth.MEMBER);
    }

    @Test
    @DisplayName("권한 포함 멤버 확인 - 실패")
    void checkProjectMemberWithAuth_fail() {
        given(projectMemberRepository.existsByProject_IdAndAccountIdAndAuth(1L, 100L, MembersAuth.MEMBER)).willReturn(false);
        assertThatThrownBy(() -> projectMemberService.checkProjectMemberWithAuth(1L, 100L, MembersAuth.MEMBER))
                .isInstanceOf(ProjectMemberIsNotExistException.class);
    }

    // ===== checkIncludedMember =====

    @Test
    @DisplayName("포함 멤버 확인 - 성공")
    void checkIncludedMember_success() {
        ProjectMember member = new ProjectMember(new Project("t", "d", 1L), 100L, MembersAuth.MEMBER);
        given(projectMemberRepository.findByProject_IdAndAccountId(1L, 100L)).willReturn(Optional.of(member));
        projectMemberService.checkIncludedMember(1L, 100L);
    }

    @Test
    @DisplayName("포함 멤버 확인 - 실패")
    void checkIncludedMember_fail() {
        given(projectMemberRepository.findByProject_IdAndAccountId(1L, 100L)).willReturn(Optional.empty());
        assertThatThrownBy(() -> projectMemberService.checkIncludedMember(1L, 100L))
                .isInstanceOf(ProjectMemberIsNotExistException.class);
    }

    // ===== getMemberByTaskId =====

    @Test
    @DisplayName("taskId로 멤버 조회")
    void getMemberByTaskId_success() {
        ProjectMember member = new ProjectMember(new Project("t", "d", 1L), 100L, MembersAuth.MEMBER);
        given(projectMemberRepository.findProjectMemberByTaskIdAndAccountId(1L, 100L)).willReturn(member);

        ProjectMember result = projectMemberService.getMemberByTaskId(1L, 100L);
        assertThat(result).isEqualTo(member);
    }

    // ===== isMemberIdEqualAccountId =====

    @Test
    @DisplayName("memberId와 accountId 일치 확인")
    void isMemberIdEqualAccountId_success() {
        given(projectMemberRepository.existsProjectMemberByIdAndAccountId(10L, 100L)).willReturn(true);
        assertThat(projectMemberService.isMemberIdEqualAccountId(10L, 100L)).isTrue();
    }

    // ===== getProjectMemberIdByUserId =====

    @Test
    @DisplayName("userId로 accountId 조회 - 성공")
    void getProjectMemberIdByUserId_success() {
        AccountResp resp = new AccountResp(99L, "user1", "e@e.com", "name", null, LocalDateTime.now());
        given(client.getAccountByUserId("user1")).willReturn(resp);

        Long result = projectMemberService.getProjectMemberIdByUserId("user1");
        assertThat(result).isEqualTo(99L);
    }

    @Test
    @DisplayName("userId로 accountId 조회 - null 반환")
    void getProjectMemberIdByUserId_null() {
        given(client.getAccountByUserId("unknown")).willReturn(null);
        assertThat(projectMemberService.getProjectMemberIdByUserId("unknown")).isNull();
    }

    // ===== getUserIdByAccountId =====

    @Test
    @DisplayName("accountId로 userId 조회")
    void getUserIdByAccountId_success() {
        AccountResp resp = new AccountResp(100L, "user1", "e@e.com", "name", null, LocalDateTime.now());
        given(client.getAccountById(100L)).willReturn(resp);

        String result = projectMemberService.getUserIdByAccountId(100L);
        assertThat(result).isEqualTo("user1");
    }

    // ===== getAccountRespMap =====

    @Test
    @DisplayName("getAccountRespMap - 빈 리스트")
    void getAccountRespMap_empty() {
        Map<Long, AccountResp> result = projectMemberService.getAccountRespMap(List.of());
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("getAccountRespMap - 멤버 있음")
    void getAccountRespMap_withMembers() {
        Project project = new Project("t", "d", 1L);
        ProjectMember member = new ProjectMember(project, 100L, MembersAuth.MEMBER);
        ReflectionTestUtils.setField(member, "id", 10L);

        AccountResp resp = new AccountResp(100L, "user1", "e@e.com", "name", null, LocalDateTime.now());
        given(client.getAccountByIds(anyList())).willReturn(Map.of(100L, resp));

        Map<Long, AccountResp> result = projectMemberService.getAccountRespMap(List.of(member));
        assertThat(result).containsKey(10L);
        assertThat(result.get(10L).userId()).isEqualTo("user1");
    }

    // ===== getUserIdsByMemberId =====

    @Test
    @DisplayName("memberIds로 userId 맵 조회")
    void getUserIdsByMemberId_success() {
        Project project = new Project("t", "d", 1L);
        ProjectMember member = new ProjectMember(project, 100L, MembersAuth.MEMBER);
        ReflectionTestUtils.setField(member, "id", 10L);

        AccountResp resp = new AccountResp(100L, "user1", "e@e.com", "name", null, LocalDateTime.now());
        given(projectMemberRepository.findProjectMembersByIdIn(List.of(10L))).willReturn(List.of(member));
        given(client.getAccountByIds(anyList())).willReturn(Map.of(100L, resp));

        Map<Long, String> result = projectMemberService.getUserIdsByMemberId(List.of(10L));
        assertThat(result).containsEntry(10L, "user1");
    }

    // ===== addProjectMember =====

    @Test
    @DisplayName("멤버 추가 - 신규 멤버")
    void addProjectMember_success_newMember() {
        Project project = new Project("t", "d", 1L);
        MemberRequestDto dto = new MemberRequestDto(null, "user1", MembersAuth.MEMBER);
        AccountResp resp = new AccountResp(200L, "user1", "e@e.com", "name", null, LocalDateTime.now());

        given(client.getAccountByUserId("user1")).willReturn(resp);
        given(projectMemberRepository.findByProject_IdAndAccountId(project.getId(), 200L))
                .willReturn(Optional.empty());

        ProjectMember newMember = new ProjectMember(project, 200L, MembersAuth.MEMBER);
        given(projectMemberRepository.save(any(ProjectMember.class))).willReturn(newMember);

        projectMemberService.addProjectMember(project, dto);

        then(projectMemberRepository).should().save(any(ProjectMember.class));
    }

    @Test
    @DisplayName("멤버 추가 - 삭제된 멤버 복구")
    void addProjectMember_success_restore() {
        Project project = new Project("t", "d", 1L);
        MemberRequestDto dto = new MemberRequestDto(null, "user1", MembersAuth.MEMBER);
        AccountResp resp = new AccountResp(200L, "user1", "e@e.com", "name", null, LocalDateTime.now());

        ProjectMember deletedMember = new ProjectMember(project, 200L, MembersAuth.MEMBER);
        deletedMember.delete();

        given(client.getAccountByUserId("user1")).willReturn(resp);
        given(projectMemberRepository.findByProject_IdAndAccountId(project.getId(), 200L))
                .willReturn(Optional.of(deletedMember));

        projectMemberService.addProjectMember(project, dto);

        assertThat(deletedMember.isDeleted()).isFalse();
    }

    @Test
    @DisplayName("멤버 추가 - 이미 존재하는 멤버")
    void addProjectMember_fail_alreadyExists() {
        Project project = new Project("t", "d", 1L);
        MemberRequestDto dto = new MemberRequestDto(null, "user1", MembersAuth.MEMBER);
        AccountResp resp = new AccountResp(200L, "user1", "e@e.com", "name", null, LocalDateTime.now());

        ProjectMember existingMember = new ProjectMember(project, 200L, MembersAuth.MEMBER);

        given(client.getAccountByUserId("user1")).willReturn(resp);
        given(projectMemberRepository.findByProject_IdAndAccountId(project.getId(), 200L))
                .willReturn(Optional.of(existingMember));

        assertThatThrownBy(() -> projectMemberService.addProjectMember(project, dto))
                .isInstanceOf(AlreadyProjectMemberExistException.class);
    }

    // ===== updateProjectMember =====

    @Test
    @DisplayName("멤버 권한 수정 - 성공")
    void updateProjectMember_success() {
        Project project = new Project("t", "d", 1L);
        ProjectMember member = new ProjectMember(project, 100L, MembersAuth.MEMBER);
        ReflectionTestUtils.setField(member, "id", 10L);

        MemberRequestDto dto = new MemberRequestDto(null, null, MembersAuth.ADMIN);
        given(projectMemberRepository.findProjectMemberById(10L)).willReturn(member);
        given(projectMemberRepository.save(member)).willReturn(member);

        projectMemberService.updateProjectMember(10L, dto);
        assertThat(member.getAuth()).isEqualTo(MembersAuth.ADMIN);
    }

    // ===== deleteProjectMember =====

    @Test
    @DisplayName("멤버 삭제 - 성공")
    void deleteProjectMember_success() {
        Project project = new Project("t", "d", 1L);
        ReflectionTestUtils.setField(project, "id", 5L);
        ProjectMember member = new ProjectMember(project, 100L, MembersAuth.MEMBER);
        ReflectionTestUtils.setField(member, "id", 10L);

        given(projectMemberRepository.findById(10L)).willReturn(Optional.of(member));

        projectMemberService.deleteProjectMember(5L, 10L);

        assertThat(member.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("멤버 삭제 - 실패 (멤버 없음)")
    void deleteProjectMember_fail_notFound() {
        given(projectMemberRepository.findById(10L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> projectMemberService.deleteProjectMember(5L, 10L))
                .isInstanceOf(ProjectMemberIsNotExistException.class);
    }

    @Test
    @DisplayName("멤버 삭제 - 실패 (다른 프로젝트)")
    void deleteProjectMember_fail_wrongProject() {
        Project project = new Project("t", "d", 1L);
        ReflectionTestUtils.setField(project, "id", 5L);
        ProjectMember member = new ProjectMember(project, 100L, MembersAuth.MEMBER);
        ReflectionTestUtils.setField(member, "id", 10L);

        given(projectMemberRepository.findById(10L)).willReturn(Optional.of(member));

        assertThatThrownBy(() -> projectMemberService.deleteProjectMember(99L, 10L))
                .isInstanceOf(NoAuthoProjectException.class);
    }

    // ===== getProjectMemberByAccountId =====

    @Test
    @DisplayName("accountId로 멤버 목록 조회")
    void getProjectMemberByAccountId_success() {
        ProjectMember member = new ProjectMember(new Project("t", "d", 1L), 100L, MembersAuth.MEMBER);
        given(projectMemberRepository.findAllByAccountId(100L)).willReturn(List.of(member));

        List<ProjectMember> result = projectMemberService.getProjectMemberByAccountId(100L);
        assertThat(result).hasSize(1);
    }

    // ===== getProjectMemberByProjectIdAndAccountId =====

    @Test
    @DisplayName("projectId와 accountId로 멤버 조회 - 성공")
    void getProjectMemberByProjectIdAndAccountId_success() {
        ProjectMember member = new ProjectMember(new Project("t", "d", 1L), 100L, MembersAuth.MEMBER);
        given(projectMemberRepository.findByProject_IdAndAccountId(1L, 100L)).willReturn(Optional.of(member));

        ProjectMember result = projectMemberService.getProjectMemberByProjectIdAndAccountId(1L, 100L);
        assertThat(result).isEqualTo(member);
    }

    @Test
    @DisplayName("projectId와 accountId로 멤버 조회 - null 반환")
    void getProjectMemberByProjectIdAndAccountId_null() {
        given(projectMemberRepository.findByProject_IdAndAccountId(1L, 100L)).willReturn(Optional.empty());

        ProjectMember result = projectMemberService.getProjectMemberByProjectIdAndAccountId(1L, 100L);
        assertThat(result).isNull();
    }
}