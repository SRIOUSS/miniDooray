package com.nhnacademy.minidooraytask.project.respository;

import com.nhnacademy.minidooraytask.project.domain.Project;
import com.nhnacademy.minidooraytask.project.domain.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    //계정 아이디로 내가 소속된 모든 프로젝트 목록 조회
    @Query("SELECT p FROM Project p JOIN ProjectMember m ON p = m.project WHERE m.accountId = :accountId")
    List<Project> findAllByAccountIdByQuery(Long accountId);

    //모든 프로젝트 가져오기
    @Query("SELECT p FROM Project p")
    List<Project> findAllProjects();

    //프로젝트 상세 조회
    @Query("SELECT p FROM Project p WHERE p.id = :projectId")
    Optional<Project> findByProjectId(@Param("projectId") Long projectId);

    //프로젝트 상태
    @Modifying
    @Query("UPDATE Project p SET p.status = :status WHERE p.id = :projectId")
    int updateProjectStatus(@Param("projectId") Long projectId,
                            @Param("status") ProjectStatus status);

}
