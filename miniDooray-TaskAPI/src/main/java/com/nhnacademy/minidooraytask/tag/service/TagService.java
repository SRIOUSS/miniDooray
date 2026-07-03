package com.nhnacademy.minidooraytask.tag.service;

import com.nhnacademy.minidooraytask.member.exception.ProjectMemberIsNotExistException;
import com.nhnacademy.minidooraytask.member.repository.ProjectMemberRepository;
import com.nhnacademy.minidooraytask.tag.domain.*;
import com.nhnacademy.minidooraytask.tag.exception.AlreadyTagExistException;
import com.nhnacademy.minidooraytask.tag.exception.TagIsNotExistException;
import com.nhnacademy.minidooraytask.tag.repository.TagRepository;
import com.nhnacademy.minidooraytask.tag.repository.TaskTagRepository;
import com.nhnacademy.minidooraytask.task.domain.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final TaskTagRepository taskTagRepository; // ★ 자식(연결 고리) 데이터를 지우기 위해 의존성 주입 추가

    @Transactional
    public Tag findOrCreateTag(String tagName) {
        return tagRepository.findByName(tagName)
                .orElseGet(() -> tagRepository.save(new Tag(tagName)));
    }

    // 권한 검증 공통 로직
    private void verifyProjectMember(Long projectId, Long accountId) {
        projectMemberRepository.findByProject_IdAndAccountId(projectId, accountId)
                .filter(member -> !member.isDeleted())
                .orElseThrow(() -> new ProjectMemberIsNotExistException("해당 프로젝트의 멤버가 아닙니다."));
    }

    // 특정 프로젝트의 태그 목록 조회
    @Transactional(readOnly = true)
    public List<TagResponseDto> getTags(Long projectId, Long accountId) {
        verifyProjectMember(projectId, accountId);

        return tagRepository.findAllByProjectId(projectId).stream()
                .map(t -> new TagResponseDto(t.getId(), t.getName()))
                .toList();
    }

    // 태그 생성
    @Transactional
    public TagResponseDto addTag(Long projectId, Long accountId, TagCreateRequestDto request) {
        verifyProjectMember(projectId, accountId);

        // 태그 이름 중복 검사
        if(tagRepository.existsByName(request.name())) {
            log.debug("[tag service] - 이미 존재하는 태그입니다 - name : {}", request.name());
            throw new AlreadyTagExistException("[tag service] 이미 존재하는 태그입니다");
        }

        Tag tag = new Tag(request.name());
        Tag savedTag = tagRepository.save(tag);

        return new TagResponseDto(savedTag.getId(), savedTag.getName());
    }

    // 태그 수정
    @Transactional
    public void updateTag(Long projectId, Long tagId, Long accountId, TagUpdateRequestDto request) {
        verifyProjectMember(projectId, accountId);

        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new TagIsNotExistException("[tag service] 존재하지 않는 태그입니다"));

        tag.update(request.name());
    }

    // 태그 삭제
    @Transactional
    public void deleteTag(Long projectId, Long tagId, Long accountId) {
        verifyProjectMember(projectId, accountId);

        if(!tagRepository.existsById(tagId)){
            log.debug("[tag service] - 존재하지 않는 태그입니다 - tagId : {}", tagId);
            throw new TagIsNotExistException("[tag service] 존재하지 않는 태그입니다");
        }

        //Task와 연결된 매핑 정보를 삭제
        taskTagRepository.deleteAllByTag_Id(tagId);

        // 그 다음 실제 태그 엔티티를 삭제
        tagRepository.deleteById(tagId);
    }

    @Transactional
    public void connectTag(Task task, List<String> tagNames) {

        Set<String> inputTagNames = new HashSet<>(tagNames);
        List<TaskTag> taskTagList = task.getTaskTagList();

        Set<String> connectTagNames;

        if(!taskTagList.isEmpty()) {
            List<Tag> beforeTags = taskTagList.stream()
                    .map(TaskTag::getTag)
                    .toList();

            List<Tag> disConnectTags = beforeTags.stream()
                    .filter(t -> !inputTagNames.contains(t.getName()))
                    .toList();

            Set<Long> disConnectTagIds = disConnectTags.stream()
                    .map(Tag::getId)
                    .collect(Collectors.toSet());

            List<TaskTag> deleteTaskTags = taskTagList.stream()
                    .filter(tt -> disConnectTagIds.contains(tt.getTag().getId()))
                    .toList();

            task.getTaskTagList().removeAll(deleteTaskTags);
            disConnectTags.forEach(t -> t.getTaskTagList().removeAll(deleteTaskTags));
            taskTagRepository.deleteAll(deleteTaskTags);

            Set<String> connectedTagNames = beforeTags.stream()
                    .map(Tag::getName)
                    .filter(name ->
                            !disConnectTags.stream()
                                    .map(Tag::getName)
                                    .collect(Collectors.toSet())
                                    .contains(name))
                    .collect(Collectors.toSet());
            connectTagNames = inputTagNames.stream().filter(tn -> !connectedTagNames.contains(tn)).collect(Collectors.toSet());
        } else {
            connectTagNames = inputTagNames;
        }

        if(!connectTagNames.isEmpty()) {
            List<Tag> existTags = tagRepository.findAllByNameIn(connectTagNames);

            Set<String> existedTagNames;
            if(existTags.isEmpty()) {
                existedTagNames = new HashSet<>();
            } else {
                existedTagNames = existTags.stream().map(Tag::getName).collect(Collectors.toSet());
            }

            List<String> notExistTagName = connectTagNames.stream().filter(tn -> !existedTagNames.contains(tn)).toList();

            if (!notExistTagName.isEmpty()) {
                List<Tag> createTag = new ArrayList<>();
                for (String name : notExistTagName) {
                    Tag tag = new Tag(name);
                    createTag.add(tag);
                }

                existTags.addAll(tagRepository.saveAll(createTag));
            }

            List<TaskTag> createTaskTag = new ArrayList<>();
            for (Tag t : existTags) {
                TaskTag taskTag = new TaskTag(task, t);
                createTaskTag.add(taskTag);
                t.getTaskTagList().add(taskTag);
            }

            if(!createTaskTag.isEmpty()) {
                taskTagRepository.saveAll(createTaskTag);
                task.getTaskTagList().addAll(createTaskTag);
            }
        }
    }
}