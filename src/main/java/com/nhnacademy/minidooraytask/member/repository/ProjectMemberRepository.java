package com.nhnacademy.minidooraytask.member.repository;

import com.nhnacademy.minidooraytask.member.domain.ProjectMember;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {

        // 프로젝트의 멤버 목록 조회 (삭제 안된 멤버만)
        @Query("SELECT m FROM ProjectMember m WHERE m.project.id = :projectId AND m.isDeleted = false")
        List<ProjectMember> findAllByProjectId(@Param("projectId") Long projectId);

        // 멤버 추가 시 이미 존재하는지 확인
        @Query("SELECT m FROM ProjectMember m WHERE m.project.id = :projectId AND m.accountId = :accountId")
        Optional<ProjectMember> findByProjectIdAndAccountId(@Param("projectId") Long projectId,
                                                            @Param("accountId") Long accountId);

        // 소프트 삭제
        @Modifying
        @Query("UPDATE ProjectMember m SET m.isDeleted = true WHERE m.id = :memberId")
        void softDeleteById(@Param("memberId") Long memberId);

}
