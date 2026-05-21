package com.nhnacademy.minidooraytask.task.repository;

import com.nhnacademy.minidooraytask.task.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    // 특정 프로젝트에 속한 모든 Task 목록 조회
//    @Query("SELECT t FROM Task t WHERE t.project.id = ?1 AND (?2 = true OR t.isDeleted = false)")
    @Query(value = "SELECT * FROM Task t WHERE t.project_id = ?1 AND (?2 = true OR t.is_deleted = false)", nativeQuery = true)
    List<Task> findAllByProject_Id(Long projectId, boolean isDeleted);

    // 특정 프로젝트에 속한 특정 Task 상세 조회 (검증용)
    Optional<Task> findByIdAndProject_Id(Long taskId, Long projectId);

    //테스크 작성자인지 확인
    @Query("SELECT EXISTS(SELECT 1 FROM Task t JOIN ProjectMember m ON t.projectMember.id = m.id WHERE t.id = ?1 AND m.id = ?2)")
    boolean existsByIdAndAccountId(long taskId, long memberId);

    Task findTaskById(Long id);

    List<Task> findByProjectMember_AccountId(Long accountId);

    boolean existsTaskByProjectMember_AccountIdAndId(Long projectMemberAccountId, Long id);

    @Query("SELECT EXISTS(SELECT 1 FROM Project p JOIN Task t ON t.project.id = p.id JOIN ProjectMember pm ON pm.project.id = p.id WHERE t.id = ?1 AND pm.accountId = ?2)")
    boolean existsByIdAndProject_ProjectMemberListIsAccountId(Long taskId, Long accountId);
}
