package com.nhnacademy.minidooraytask.tag.service;

import com.nhnacademy.minidooraytask.member.exception.ProjectMemberIsNotExistException;
import com.nhnacademy.minidooraytask.member.repository.ProjectMemberRepository;
import com.nhnacademy.minidooraytask.tag.domain.Tag;
import com.nhnacademy.minidooraytask.tag.domain.TagCreateRequestDto;
import com.nhnacademy.minidooraytask.tag.domain.TagResponseDto;
import com.nhnacademy.minidooraytask.tag.domain.TagUpdateRequestDto;
import com.nhnacademy.minidooraytask.tag.exception.AlreadyTagExistException;
import com.nhnacademy.minidooraytask.tag.exception.TagIsNotExistException;
import com.nhnacademy.minidooraytask.tag.repository.TagRepository;
import com.nhnacademy.minidooraytask.tag.repository.TaskTagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final TaskTagRepository taskTagRepository; // ★ 자식(연결 고리) 데이터를 지우기 위해 의존성 주입 추가

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
                .map(TagResponseDto::from)
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

        return TagResponseDto.from(savedTag);
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
}