package com.nhnacademy.minidooraytask.tag.service;


import com.nhnacademy.minidooraytask.member.exception.ProjectMemberIsNotExistException;
import com.nhnacademy.minidooraytask.tag.domain.Tag;
import com.nhnacademy.minidooraytask.tag.domain.TagResponseDto;
import com.nhnacademy.minidooraytask.tag.exception.AlreadyTagExistException;
import com.nhnacademy.minidooraytask.tag.exception.TagIsNotExistException;
import com.nhnacademy.minidooraytask.tag.repository.TagRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    //GET
    @Transactional
    public List<TagResponseDto> getTags(Long taskId) {

        return tagRepository.findAllByTaskId(taskId).
                 stream()
                 .map(TagResponseDto::from)
                 .toList();
    }

    //POST
    @Transactional
    public void addTag(Long tagId, String name) {
        if(!tagRepository.existsTagsByIdAndName(tagId, name)) {
            log.debug("[tag service] - 이미 작성하신 태그 입니다 - tagId : {}, name : {}", tagId, name);
            throw new AlreadyTagExistException("[tag service] 이미 존재하는 태그 입니다");
        }

        Tag tag = new Tag(name);
        tagRepository.save(tag);
    }

    //DELETE
    @Transactional
    public void deleteTag(Long tagId) {
        if(!tagRepository.existsTagById(tagId)){
            log.debug("[tag service] - 존재하지 않는 태그 입니다 - tagId : {}", tagId);
            throw new TagIsNotExistException("[tag service] 존재하지 않는 태그 입니다");
        }

        tagRepository.deleteById(tagId);
    }

}
