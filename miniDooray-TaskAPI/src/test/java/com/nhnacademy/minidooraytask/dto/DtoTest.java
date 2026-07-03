package com.nhnacademy.minidooraytask.dto;

import com.nhnacademy.minidooraytask.client.dto.account.AccountListReq;
import com.nhnacademy.minidooraytask.client.dto.account.AccountListResp;
import com.nhnacademy.minidooraytask.client.dto.account.AccountResp;
import com.nhnacademy.minidooraytask.member.domain.*;
import com.nhnacademy.minidooraytask.milestone.domain.*;
import com.nhnacademy.minidooraytask.project.domain.*;
import com.nhnacademy.minidooraytask.task.domain.TaskCreateRequestDto;
import com.nhnacademy.minidooraytask.task.domain.TaskUpdateRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DtoTest {

    @Test
    void projectCreateRequestDto() {
        ProjectCreateRequestDto dto = new ProjectCreateRequestDto();
        assertThat(dto).isNotNull();
    }

    @Test
    void projectUpdateRequestDto() {
        ProjectUpdateRequestDto dto = new ProjectUpdateRequestDto("제목", "설명", ProjectStatus.ACTIVE);
        assertThat(dto.title()).isEqualTo("제목");
        assertThat(dto.description()).isEqualTo("설명");
        assertThat(dto.status()).isEqualTo(ProjectStatus.ACTIVE);
    }

    @Test
    void accountListReq() {
        AccountListReq req = new AccountListReq(List.of(1L, 2L));
        assertThat(req.accountIdList()).hasSize(2);
    }

    @Test
    void accountListResp() {
        AccountResp accountResp = new AccountResp(1L, "user1", "e@e.com", "name", null, LocalDateTime.now());
        AccountListResp resp = new AccountListResp(List.of(accountResp));
        assertThat(resp.accountRespList()).hasSize(1);
    }

    @Test
    void milestoneCreateRequestDto() {
        LocalDateTime due = LocalDateTime.now().plusDays(1);
        MilestoneCreateRequestDto dto = new MilestoneCreateRequestDto("제목", "설명", MileStoneStatus.PLANNED, due);
        assertThat(dto.title()).isEqualTo("제목");
        assertThat(dto.status()).isEqualTo(MileStoneStatus.PLANNED);
    }

    @Test
    void milestoneUpdateRequestDto() {
        LocalDateTime due = LocalDateTime.now().plusDays(1);
        MilestoneUpdateRequestDto dto = new MilestoneUpdateRequestDto("제목", "설명", MileStoneStatus.IN_PROGRESS, due);
        assertThat(dto.title()).isEqualTo("제목");
        assertThat(dto.status()).isEqualTo(MileStoneStatus.IN_PROGRESS);
    }

    @Test
    void milestoneInfoResponseDto() {
        MilestoneInfoResponseDto dto = new MilestoneInfoResponseDto(1L, MileStoneStatus.PLANNED);
        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.status()).isEqualTo(MileStoneStatus.PLANNED);
    }

    @Test
    void milestoneListResponseDto() {
        MilestoneInfoResponseDto info = new MilestoneInfoResponseDto(1L, MileStoneStatus.PLANNED);
        MilestoneListResponseDto dto = new MilestoneListResponseDto(10L, List.of(info));
        assertThat(dto.projectId()).isEqualTo(10L);
        assertThat(dto.milestoneList()).hasSize(1);
    }

    @Test
    void projectMemberAddRequestDto() {
        ProjectMemberAddRequestDto dto = new ProjectMemberAddRequestDto(100L);
        assertThat(dto.accountId()).isEqualTo(100L);
    }

    @Test
    void projectMemberResponseDto_from() {
        Project project = new Project("t", "d", 1L);
        ReflectionTestUtils.setField(project, "id", 5L);
        ProjectMember member = new ProjectMember(project, 100L, MembersAuth.MEMBER);
        ReflectionTestUtils.setField(member, "id", 10L);

        ProjectMemberResponseDto dto = ProjectMemberResponseDto.from(member);

        assertThat(dto.getProjectMemberId()).isEqualTo(10L);
        assertThat(dto.getProjectId()).isEqualTo(5L);
        assertThat(dto.getAccountId()).isEqualTo(100L);
        assertThat(dto.getAuth()).isEqualTo(MembersAuth.MEMBER);
    }

    @Test
    void taskCreateRequestDto() {
        TaskCreateRequestDto dto = new TaskCreateRequestDto("제목", "내용", 1L, List.of(1L, 2L));
        assertThat(dto.title()).isEqualTo("제목");
        assertThat(dto.tagIds()).hasSize(2);
    }

    @Test
    void taskUpdateRequestDto() {
        TaskUpdateRequestDto dto = new TaskUpdateRequestDto("수정제목", "수정내용", null, List.of());
        assertThat(dto.title()).isEqualTo("수정제목");
        assertThat(dto.milestoneId()).isNull();
    }
}