package com.nhnacademy.minidooraytask.MileStone.repository;

import com.nhnacademy.minidooraytask.MileStone.domain.MileStone;
import com.nhnacademy.minidooraytask.MileStone.domain.MileStoneListProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MileStoneRepository extends JpaRepository<MileStone, Long> {

    @Query("SELECT ms FROM Task t JOIN MileStone ms ON ms.task.id = t.id WHERE t.project.id = ?1")
    List<MileStone> getMileStoneByProjectId(long projectId);

    @Query("SELECT t.project.id projectId, ms.id mileStoneId , ms.status status FROM Task t JOIN MileStone ms ON ms.task.id = t.id WHERE t.project.id IN :projectIds")
    List<MileStoneListProjection> getMileStoneListByProjectIds (@Param("projectIds") List<Long> projectIds);

    boolean existsMileStoneByTask_Id(Long taskId);

    MileStone findMileStoneByTask_Id(Long taskId);

    void deleteMileStoneByTask_Id(Long taskId);
}
