package com.nhnacademy.minidooraytask.tag.repository;

import com.nhnacademy.minidooraytask.tag.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {

    //특정 프로젝트(:projectId)에 포함된 모든 작업(Task)들과 연결된 태그(Tag) 목록을 가져오기
    @Query("SELECT t FROM Tag t JOIN TaskTag tt ON tt.tag = t JOIN Task ta ON tt.task = ta WHERE ta.id = :taskId")
    List<Tag> findAllByTaskId(@Param("projectId") Long taskId);

    boolean existsTagsByIdAndName(Long id, String name);

    boolean existsTagById(Long id);
}
