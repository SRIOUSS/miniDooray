package com.nhnacademy.minidooraytask.tag.repository;

import com.nhnacademy.minidooraytask.tag.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

    // 해당 프로젝트에 포함된 모든 Task들이 사용하는 태그 목록 조회
    @Query("SELECT DISTINCT t FROM Tag t JOIN TaskTag tt ON tt.tag = t JOIN tt.task ta WHERE ta.project.id = :projectId")
    List<Tag> findAllByProjectId(@Param("projectId") Long projectId);

    // 태그 이름으로만 중복 검사
    boolean existsByName(String name);

    // 태그이름으로 실제 Tag 객체 가져오기
    Optional<Tag> findByName(String name);
}
