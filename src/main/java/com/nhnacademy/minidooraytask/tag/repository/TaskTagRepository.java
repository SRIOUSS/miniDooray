package com.nhnacademy.minidooraytask.tag.repository;

import com.nhnacademy.minidooraytask.tag.domain.TaskTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskTagRepository extends JpaRepository<TaskTag, Long> {

    //특정 Task에 연결된 모든 태그들 조회 (GET 요청)
    List<TaskTag> findAllByTask_Id(Long taskId);

    //특정 Task에 연결된 Tag 일괄 삭제
    void deleteAllByTask_Id(Long taskId);

    // 특정 Tag가 삭제될 때, 해당 태그가 달린 모든 Task와의 연결 고리를 일괄 삭제
    void deleteAllByTag_Id(Long tagId);

}
