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

       
//       @Query("SELECT m FROM ProjectMember m WHERE m.project.id = :projectId AND m.isDeleted = false")
//        List<ProjectMember> findByProjectId(@Param("projectId") Long projectId);

        // projectId, accountId로 특정 멤버 조회
        Optional<ProjectMember> findByProject_IdAndAccountId(Long projectId, Long accountId);

        List<ProjectMember> findProjectMembersByIdIn(List<Long> ids);

        @Query("SELECT EXISTS(SELECT 1 FROM Project p JOIN ProjectMember pm ON p.id = pm.project.id WHERE p.id = :projectId AND pm.accountId = :accountId)")
        boolean existsByProject_IdAndAccountId(@Param("projectId") Long projectId, @Param("accountId") Long accountId);

        List<ProjectMember> findProjectMembersByProject_Id(Long projectId);

        boolean existsByProject_IdAndAccountIdAndAuth(Long projectId, Long accountId, MembersAuth auth);

        boolean existsByProject_IdAndIdAndAccountId(Long projectId, Long id, Long accountId);

        ProjectMember findProjectMemberById(Long id);

        boolean existsProjectMemberByIdAndAccountId(Long id, Long accountId);

        List<ProjectMember> findAllById(Long id);

        @Query("SELECT pm FROM Project p JOIN Task t ON t.project.id = p.id JOIN ProjectMember pm ON pm.project.id = p.id WHERE t.id = ?1 AND pm.accountId = ?2")
        ProjectMember findProjectMemberByTaskIdAndAccountId(Long taskId, Long accountId);

        List<ProjectMember> findAllByAccountId(Long accountId);

        @Query("SELECT pm.auth FROM ProjectMember pm WHERE pm.project.id = ?1 AND pm.accountId = ?2")
        String getAuth(long projectId, long accountId);

        @Query("SELECT pm FROM Project p JOIN ProjectMember pm ON p.id = pm.project.id WHERE p.id = ?1 AND pm.accountId = ?2")
        Optional<ProjectMember> findProjectMemberByProject_IdAndAccountId(Long projectId, Long accountId);
}
