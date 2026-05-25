package com.nhnacademy.minidooraytask.service;

import com.nhnacademy.minidooraytask.member.domain.MembersAuth;
import com.nhnacademy.minidooraytask.member.domain.ProjectMember;
import com.nhnacademy.minidooraytask.member.exception.ProjectMemberIsNotExistException;
import com.nhnacademy.minidooraytask.member.repository.ProjectMemberRepository;
import com.nhnacademy.minidooraytask.project.domain.Project;
import com.nhnacademy.minidooraytask.tag.domain.Tag;
import com.nhnacademy.minidooraytask.tag.domain.TagCreateRequestDto;
import com.nhnacademy.minidooraytask.tag.domain.TagResponseDto;
import com.nhnacademy.minidooraytask.tag.domain.TaskTag;
import com.nhnacademy.minidooraytask.tag.exception.AlreadyTagExistException;
import com.nhnacademy.minidooraytask.tag.repository.TagRepository;
import com.nhnacademy.minidooraytask.tag.repository.TaskTagRepository;
import com.nhnacademy.minidooraytask.tag.service.TagService;
import com.nhnacademy.minidooraytask.task.domain.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {

    @InjectMocks
    private TagService tagService;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private ProjectMemberRepository projectMemberRepository;

    @Mock
    private TaskTagRepository taskTagRepository;

    @Test
    @DisplayName("태그 추가 - 성공")
    void addTag_success() {
        Long projectId = 1L;
        Long accountId = 100L;
        TagCreateRequestDto request = new TagCreateRequestDto("백엔드");


        ProjectMember mockMember = mock(ProjectMember.class);
        given(mockMember.isDeleted()).willReturn(false); // 삭제되지 않은 정상 멤버
        given(projectMemberRepository.findByProject_IdAndAccountId(projectId, accountId))
                .willReturn(Optional.of(mockMember));
        given(tagRepository.existsByName(request.name())).willReturn(false);

        Tag savedTag = new Tag("백엔드");
        ReflectionTestUtils.setField(savedTag, "id", 10L);
        given(tagRepository.save(any(Tag.class))).willReturn(savedTag);

        TagResponseDto result = tagService.addTag(projectId, accountId, request);

        assertThat(result.id()).isEqualTo(10L);
        assertThat(result.name()).isEqualTo("백엔드");

        then(projectMemberRepository).should().findByProject_IdAndAccountId(projectId, accountId);
        then(tagRepository).should().save(any(Tag.class));
    }

    @Test
    @DisplayName("태그 추가 실패 - 중복 태그")
    void addTag_fail_alreadyExist() {

        Long projectId = 1L;
        Long accountId = 100L;
        TagCreateRequestDto request = new TagCreateRequestDto("중복태그");

        ProjectMember mockMember = mock(ProjectMember.class);
        given(mockMember.isDeleted()).willReturn(false);
        given(projectMemberRepository.findByProject_IdAndAccountId(projectId, accountId))
                .willReturn(Optional.of(mockMember));
        given(tagRepository.existsByName(request.name())).willReturn(true);

        assertThatThrownBy(() -> tagService.addTag(projectId, accountId, request))
                .isInstanceOf(AlreadyTagExistException.class);
    }

    @Test
    @DisplayName("권한 검증 실패 - 프로젝트 멤버가 아닐 때")
    void verifyProjectMember_fail() {
        Long projectId = 1L;
        Long accountId = 999L;
        TagCreateRequestDto request = new TagCreateRequestDto("태그");

        given(projectMemberRepository.findByProject_IdAndAccountId(projectId, accountId))
                .willReturn(Optional.empty());
        assertThatThrownBy(() -> tagService.addTag(projectId, accountId, request))
                .isInstanceOf(ProjectMemberIsNotExistException.class);
    }

    @Test
    @DisplayName("Task에 새로운 태그들 연결 검증")
    void connectTag_success_newTags() {
        Task task = new Task(mock(Project.class), mock(ProjectMember.class), "태스크", "내용");
        List<String> inputTags = List.of("신규태그1", "신규태그2");

        given(tagRepository.findAllByNameIn(anySet())).willReturn(new ArrayList<>());

        Tag savedTag1 = new Tag("신규태그1");
        ReflectionTestUtils.setField(savedTag1, "id", 1L);
        Tag savedTag2 = new Tag("신규태그2");
        ReflectionTestUtils.setField(savedTag2, "id", 2L);

        given(tagRepository.saveAll(anyList())).willReturn(List.of(savedTag1, savedTag2));

        given(taskTagRepository.saveAll(anyList())).willReturn(anyList());

        tagService.connectTag(task, inputTags);

        assertThat(task.getTaskTagList()).hasSize(2);

        then(tagRepository).should().findAllByNameIn(anySet());
        then(tagRepository).should().saveAll(anyList());
        then(taskTagRepository).should().saveAll(anyList());
    }

    @Test
    @DisplayName("태그 조회 - 성공")
    void getTags_success() {
        Long projectId = 1L;
        Long accountId = 100L;

        ProjectMember mockMember = mock(ProjectMember.class);
        given(mockMember.isDeleted()).willReturn(false);
        given(projectMemberRepository.findByProject_IdAndAccountId(projectId, accountId))
                .willReturn(Optional.of(mockMember));

        Tag tag = new Tag("태그1");
        ReflectionTestUtils.setField(tag, "id", 1L);
        given(tagRepository.findAllByProjectId(projectId)).willReturn(List.of(tag));

        List<com.nhnacademy.minidooraytask.tag.domain.TagResponseDto> result = tagService.getTags(projectId, accountId);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().name()).isEqualTo("태그1");
    }

    @Test
    @DisplayName("태그 조회 - 실패 (프로젝트 멤버 아님)")
    void getTags_fail_notMember() {
        Long projectId = 1L;
        Long accountId = 999L;

        given(projectMemberRepository.findByProject_IdAndAccountId(projectId, accountId))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> tagService.getTags(projectId, accountId))
                .isInstanceOf(com.nhnacademy.minidooraytask.member.exception.ProjectMemberIsNotExistException.class);
    }

    @Test
    @DisplayName("태그 수정 - 성공")
    void updateTag_success() {
        Long projectId = 1L;
        Long tagId = 10L;
        Long accountId = 100L;

        ProjectMember mockMember = mock(ProjectMember.class);
        given(mockMember.isDeleted()).willReturn(false);
        given(projectMemberRepository.findByProject_IdAndAccountId(projectId, accountId))
                .willReturn(Optional.of(mockMember));

        Tag tag = new Tag("기존태그");
        ReflectionTestUtils.setField(tag, "id", tagId);
        given(tagRepository.findById(tagId)).willReturn(Optional.of(tag));

        tagService.updateTag(projectId, tagId, accountId, new com.nhnacademy.minidooraytask.tag.domain.TagUpdateRequestDto("수정태그"));

        assertThat(tag.getName()).isEqualTo("수정태그");
    }

    @Test
    @DisplayName("태그 수정 - 실패 (태그 없음)")
    void updateTag_fail_notFound() {
        Long projectId = 1L;
        Long tagId = 10L;
        Long accountId = 100L;

        ProjectMember mockMember = mock(ProjectMember.class);
        given(mockMember.isDeleted()).willReturn(false);
        given(projectMemberRepository.findByProject_IdAndAccountId(projectId, accountId))
                .willReturn(Optional.of(mockMember));
        given(tagRepository.findById(tagId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> tagService.updateTag(projectId, tagId, accountId, new com.nhnacademy.minidooraytask.tag.domain.TagUpdateRequestDto("수정태그")))
                .isInstanceOf(com.nhnacademy.minidooraytask.tag.exception.TagIsNotExistException.class);
    }

    @Test
    @DisplayName("태그 삭제 - 성공")
    void deleteTag_success() {
        Long projectId = 1L;
        Long tagId = 10L;
        Long accountId = 100L;

        ProjectMember mockMember = mock(ProjectMember.class);
        given(mockMember.isDeleted()).willReturn(false);
        given(projectMemberRepository.findByProject_IdAndAccountId(projectId, accountId))
                .willReturn(Optional.of(mockMember));
        given(tagRepository.existsById(tagId)).willReturn(true);

        tagService.deleteTag(projectId, tagId, accountId);

        then(taskTagRepository).should().deleteAllByTag_Id(tagId);
        then(tagRepository).should().deleteById(tagId);
    }

    @Test
    @DisplayName("태그 삭제 - 실패 (태그 없음)")
    void deleteTag_fail_notFound() {
        Long projectId = 1L;
        Long tagId = 10L;
        Long accountId = 100L;

        ProjectMember mockMember = mock(ProjectMember.class);
        given(mockMember.isDeleted()).willReturn(false);
        given(projectMemberRepository.findByProject_IdAndAccountId(projectId, accountId))
                .willReturn(Optional.of(mockMember));
        given(tagRepository.existsById(tagId)).willReturn(false);

        assertThatThrownBy(() -> tagService.deleteTag(projectId, tagId, accountId))
                .isInstanceOf(com.nhnacademy.minidooraytask.tag.exception.TagIsNotExistException.class);
    }

    @Test
    @DisplayName("findOrCreateTag - 기존 태그 반환")
    void findOrCreateTag_existing() {
        Tag existing = new Tag("기존태그");
        given(tagRepository.findByName("기존태그")).willReturn(Optional.of(existing));

        Tag result = tagService.findOrCreateTag("기존태그");
        assertThat(result.getName()).isEqualTo("기존태그");
    }

    @Test
    @DisplayName("findOrCreateTag - 새 태그 생성")
    void findOrCreateTag_new() {
        given(tagRepository.findByName("신규태그")).willReturn(Optional.empty());
        Tag newTag = new Tag("신규태그");
        given(tagRepository.save(any(Tag.class))).willReturn(newTag);

        Tag result = tagService.findOrCreateTag("신규태그");
        assertThat(result.getName()).isEqualTo("신규태그");
    }

    @Test
    @DisplayName("권한 검증 실패 - 삭제된 멤버")
    void addTag_fail_deletedMember() {
        Long projectId = 1L;
        Long accountId = 100L;
        TagCreateRequestDto request = new TagCreateRequestDto("태그");

        ProjectMember mockMember = mock(ProjectMember.class);
        given(mockMember.isDeleted()).willReturn(true);
        given(projectMemberRepository.findByProject_IdAndAccountId(projectId, accountId))
                .willReturn(Optional.of(mockMember));

        assertThatThrownBy(() -> tagService.addTag(projectId, accountId, request))
                .isInstanceOf(ProjectMemberIsNotExistException.class);
    }

    @Test
    @DisplayName("connectTag - 기존 태그 해제 후 새 태그 추가")
    void connectTag_existingTagDisconnectAndAddNew() {
        Project project = new Project("t", "d", 1L);
        ProjectMember member = new ProjectMember(project, 1L, MembersAuth.MEMBER);
        Task task = new Task(project, member, "태스크", "내용");

        Tag existingTag = new Tag("기존태그");
        ReflectionTestUtils.setField(existingTag, "id", 1L);
        TaskTag existingTaskTag = new TaskTag(task, existingTag);
        ReflectionTestUtils.setField(existingTaskTag, "id", 1L);
        task.getTaskTagList().add(existingTaskTag);

        List<String> inputTags = List.of("새태그");

        given(tagRepository.findAllByNameIn(anySet())).willReturn(new ArrayList<>());
        Tag newTag = new Tag("새태그");
        ReflectionTestUtils.setField(newTag, "id", 2L);
        given(tagRepository.saveAll(anyList())).willReturn(List.of(newTag));

        tagService.connectTag(task, inputTags);

        then(taskTagRepository).should().deleteAll(anyList());
        then(tagRepository).should().saveAll(anyList());
        then(taskTagRepository).should().saveAll(anyList());
    }

    @Test
    @DisplayName("connectTag - 기존 태그 유지 + 이미 존재하는 태그로 새 연결")
    void connectTag_keepExistingAndConnectAlreadyExistingTag() {
        Project project = new Project("t", "d", 1L);
        ProjectMember member = new ProjectMember(project, 1L, MembersAuth.MEMBER);
        Task task = new Task(project, member, "태스크", "내용");

        Tag existingTag = new Tag("기존태그");
        ReflectionTestUtils.setField(existingTag, "id", 1L);
        TaskTag existingTaskTag = new TaskTag(task, existingTag);
        ReflectionTestUtils.setField(existingTaskTag, "id", 1L);
        task.getTaskTagList().add(existingTaskTag);

        // 기존태그는 유지, DB에 이미 있는 "기존태그2"를 새로 추가
        List<String> inputTags = List.of("기존태그", "기존태그2");

        Tag dbTag = new Tag("기존태그2");
        ReflectionTestUtils.setField(dbTag, "id", 3L);
        given(tagRepository.findAllByNameIn(anySet())).willReturn(List.of(dbTag));

        tagService.connectTag(task, inputTags);

        then(taskTagRepository).should().deleteAll(anyList());
        then(taskTagRepository).should().saveAll(anyList());
    }
}
