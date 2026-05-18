package com.nhnacademy.minidooraytask.member.repository;

import com.nhnacademy.minidooraytask.member.domain.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {

        // 프로젝트의 멤버 목록 조회
        @Query("SELECT m FROM ProjectMember m WHERE m.project.id = :projectId AND m.isDeleted = false")
        List<ProjectMember> findAllByProjectId(@Param("projectId") Long projectId);

        // 특정 멤버 조회
        Optional<ProjectMember> findByProject_IdAndAccountId(Long projectId, Long accountId);

        // 해당 프로젝트에 해당 멤버가 있는지 확인
        boolean existsProjectMemberByProject_IdAndAccountId(Long projectId, Long accountId);

}
