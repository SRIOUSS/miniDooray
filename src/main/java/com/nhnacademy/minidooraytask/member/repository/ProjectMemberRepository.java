package com.nhnacademy.minidooraytask.member.repository;

import com.nhnacademy.minidooraytask.member.domain.MembersAuth;
import com.nhnacademy.minidooraytask.member.domain.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {

        // 프로젝트의 멤버 목록 조회
        @Query("SELECT m FROM ProjectMember m WHERE m.project.id = :projectId AND m.isDeleted = false")
        List<ProjectMember> findByProjectId(@Param("projectId") Long projectId);

        // projectId, accountId로 특정 멤버 조회
        Optional<ProjectMember> findByProject_IdAndAccountId(Long projectId, Long accountId);

        List<ProjectMember> findProjectMembersByIdIn(List<Long> ids);

        boolean existsByProject_IdAndAccountId(Long projectId, Long accountId);

        List<ProjectMember> findProjectMembersByProject_Id(Long projectId);

        boolean existsByProject_IdAndAccountIdAndAuth(Long projectId, Long accountId, MembersAuth auth);

        boolean existsByProject_IdAndIdAndAccountId(Long projectId, Long id, Long accountId);

        ProjectMember findProjectMemberById(Long id);

        boolean existsProjectMemberByIdAndAccountId(Long id, Long accountId);
}
