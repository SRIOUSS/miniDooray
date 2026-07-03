package com.nhnacademy.minidooraytask.project.respository;

import com.nhnacademy.minidooraytask.project.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    //계정 아이디로 내가 소속된 모든 프로젝트 목록 조회
    @Query("SELECT p FROM Project p JOIN ProjectMember m ON p = m.project WHERE m.accountId = ?1 AND (?2 = true OR p.isDeleted = false)")
    List<Project> findAllByAccountIdByQuery(Long accountId, boolean isDeleted);

//    //프로젝트 상세 조회
//    @Query("SELECT p FROM Project p WHERE p.id = :projectId")
//    Optional<Project> findByProjectId(@Param("projectId") Long projectId);

    @Override
    Optional<Project> findById(Long id);

    boolean existsProjectByIdAndCreateAccountId(Long id, Long createAccountId);

    Optional<Project> findProjectByIdAndCreateAccountId(Long id, Long createAccountId);
}
