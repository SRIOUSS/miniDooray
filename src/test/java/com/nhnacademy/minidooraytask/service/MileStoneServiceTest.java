package com.nhnacademy.minidooraytask.service;

import com.nhnacademy.minidooraytask.member.domain.MembersAuth;
import com.nhnacademy.minidooraytask.member.domain.ProjectMember;
import com.nhnacademy.minidooraytask.milestone.domain.MileStone;
import com.nhnacademy.minidooraytask.milestone.domain.MileStoneListProjection;
import com.nhnacademy.minidooraytask.milestone.domain.MileStoneStatus;
import com.nhnacademy.minidooraytask.milestone.domain.MilestoneListResponseDto;
import com.nhnacademy.minidooraytask.milestone.domain.MilestoneRequestDto;
import com.nhnacademy.minidooraytask.milestone.domain.MilestoneResponseDto;
import com.nhnacademy.minidooraytask.milestone.exception.MileStoneIsExistException;
import com.nhnacademy.minidooraytask.milestone.exception.MileStoneIsNotExistException;
import com.nhnacademy.minidooraytask.milestone.repository.MileStoneRepository;
import com.nhnacademy.minidooraytask.milestone.service.MileStoneService;
import com.nhnacademy.minidooraytask.project.domain.Project;
import com.nhnacademy.minidooraytask.task.domain.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class MileStoneServiceTest {

    @InjectMocks
    private MileStoneService mileStoneService;

    @Mock
    private MileStoneRepository mileStoneRepository;

    @Test
    @DisplayName("마일스톤 생성 실패")
    void createMileStone_fail_alreadyExist() {
        Task mockTask = mock(Task.class);
        given(mockTask.getId()).willReturn(1L);
        given(mileStoneRepository.existsMileStoneByTask_Id(1L)).willReturn(true);
        MilestoneRequestDto requestDto = new MilestoneRequestDto("제목", "내용", MileStoneStatus.PLANNED, LocalDateTime.now());

        assertThatThrownBy(() -> mileStoneService.createMileStone(mockTask, requestDto))
                .isInstanceOf(MileStoneIsExistException.class);
    }

    @Test
    @DisplayName("마일스톤 수정 실패")
    void updateMileStone_fail_notFound() {

        long taskId = 1L;
        given(mileStoneRepository.existsMileStoneByTask_Id(taskId)).willReturn(false);
        MilestoneRequestDto requestDto = new MilestoneRequestDto("제목", "내용", MileStoneStatus.PLANNED, LocalDateTime.now());

        assertThatThrownBy(() -> mileStoneService.updateMileStone(taskId, requestDto))
                .isInstanceOf(MileStoneIsNotExistException.class);
    }

    @Test
    @DisplayName("마일스톤 생성 성공")
    void createMileStone_success() {
        Project project = new Project("t", "d", 1L);
        ProjectMember member = new ProjectMember(project, 1L, MembersAuth.MEMBER);
        Task task = new Task(project, member, "태스크", "내용");
        MilestoneRequestDto requestDto = new MilestoneRequestDto("제목", "내용", MileStoneStatus.PLANNED, LocalDateTime.now());

        given(mileStoneRepository.existsMileStoneByTask_Id(task.getId())).willReturn(false);
        MileStone saved = MileStone.create(task, requestDto);
        given(mileStoneRepository.save(any(MileStone.class))).willReturn(saved);

        mileStoneService.createMileStone(task, requestDto);

        then(mileStoneRepository).should().save(any(MileStone.class));
    }

    @Test
    @DisplayName("마일스톤 단건 조회 성공")
    void getMileStoneByTaskId_success() {
        Project project = new Project("t", "d", 1L);
        ProjectMember member = new ProjectMember(project, 1L, MembersAuth.MEMBER);
        Task task = new Task(project, member, "태스크", "내용");
        ReflectionTestUtils.setField(task, "id", 1L);
        MilestoneRequestDto req = new MilestoneRequestDto("제목", "내용", MileStoneStatus.PLANNED, LocalDateTime.now());
        MileStone mileStone = MileStone.create(task, req);
        ReflectionTestUtils.setField(mileStone, "id", 1L);

        given(mileStoneRepository.findMileStoneByTask_Id(1L)).willReturn(mileStone);

        MilestoneResponseDto result = mileStoneService.getMileStoneByTaskId(1L);

        assertThat(result.title()).isEqualTo("제목");
    }

    @Test
    @DisplayName("프로젝트별 마일스톤 목록 조회")
    void getMileStoneListByProjectId_success() {
        Project project = new Project("t", "d", 1L);
        ProjectMember member = new ProjectMember(project, 1L, MembersAuth.MEMBER);
        Task task = new Task(project, member, "태스크", "내용");
        MilestoneRequestDto req = new MilestoneRequestDto("제목", "내용", MileStoneStatus.PLANNED, LocalDateTime.now());
        MileStone mileStone = MileStone.create(task, req);
        ReflectionTestUtils.setField(mileStone, "id", 1L);

        given(mileStoneRepository.getMileStoneByProjectId(1L)).willReturn(List.of(mileStone));

        List<MilestoneResponseDto> result = mileStoneService.getMileStoneListByProjectId(1L);

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("마일스톤 수정 성공")
    void updateMileStone_success() {
        long taskId = 1L;
        Project project = new Project("t", "d", 1L);
        ProjectMember member = new ProjectMember(project, 1L, MembersAuth.MEMBER);
        Task task = new Task(project, member, "태스크", "내용");
        MilestoneRequestDto originalReq = new MilestoneRequestDto("제목", "내용", MileStoneStatus.PLANNED, LocalDateTime.now());
        MileStone mileStone = MileStone.create(task, originalReq);

        LocalDateTime newDueDate = LocalDateTime.now().plusDays(7);
        MilestoneRequestDto updateReq = new MilestoneRequestDto("새제목", "새내용", MileStoneStatus.IN_PROGRESS, newDueDate);

        given(mileStoneRepository.existsMileStoneByTask_Id(taskId)).willReturn(true);
        given(mileStoneRepository.findMileStoneByTask_Id(taskId)).willReturn(mileStone);
        given(mileStoneRepository.save(mileStone)).willReturn(mileStone);

        mileStoneService.updateMileStone(taskId, updateReq);

        assertThat(mileStone.getTitle()).isEqualTo("새제목");
        assertThat(mileStone.getStatus()).isEqualTo(MileStoneStatus.IN_PROGRESS);
    }

    @Test
    @DisplayName("마일스톤 삭제 성공")
    void deleteMileStone_success() {
        long taskId = 1L;
        given(mileStoneRepository.existsMileStoneByTask_Id(taskId)).willReturn(true);

        mileStoneService.deleteMileStone(taskId);

        then(mileStoneRepository).should().deleteMileStoneByTask_Id(taskId);
    }

    @Test
    @DisplayName("마일스톤 삭제 실패 - 존재하지 않음")
    void deleteMileStone_fail_notExist() {
        long taskId = 1L;
        given(mileStoneRepository.existsMileStoneByTask_Id(taskId)).willReturn(false);

        assertThatThrownBy(() -> mileStoneService.deleteMileStone(taskId))
                .isInstanceOf(MileStoneIsNotExistException.class);
    }

    @Test
    @DisplayName("프로젝트 ID 목록으로 마일스톤 목록 조회")
    void getMileStoneListByProjectIds_success() {
        MileStoneListProjection projection = mock(MileStoneListProjection.class);
        given(projection.getProjectId()).willReturn(1L);
        given(projection.getMileStoneId()).willReturn(10L);
        given(projection.getStatus()).willReturn(MileStoneStatus.PLANNED);

        given(mileStoneRepository.getMileStoneListByProjectIds(any())).willReturn(List.of(projection));

        List<MilestoneListResponseDto> result = mileStoneService.getMileStoneListByProjectIds(List.of(1L));

        assertThat(result).hasSize(1);
        assertThat(result.get(0).projectId()).isEqualTo(1L);
        assertThat(result.get(0).milestoneList()).hasSize(1);
    }
}
