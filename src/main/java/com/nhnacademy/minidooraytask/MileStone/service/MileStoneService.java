package com.nhnacademy.minidooraytask.MileStone.service;

import com.nhnacademy.minidooraytask.MileStone.domain.*;
import com.nhnacademy.minidooraytask.MileStone.exception.MileStoneIsExistException;
import com.nhnacademy.minidooraytask.MileStone.exception.MileStoneIsNotExistException;
import com.nhnacademy.minidooraytask.MileStone.repository.MileStoneRepository;
import com.nhnacademy.minidooraytask.task.domain.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class MileStoneService {
    private final MileStoneRepository mileStoneRepository;

    @Transactional(readOnly = true)
    public MilestoneResponseDto getMileStoneByTaskId(long taskId) {
        MileStone mileStone = mileStoneRepository.findMileStoneByTask_Id(taskId);

        return new MilestoneResponseDto(mileStone.getId(), mileStone.getTask().getId(), mileStone.getTitle(),
                mileStone.getDescription(), mileStone.getStatus(), mileStone.getDueDate(), mileStone.getCreatedAt(),
                mileStone.getUpdatedAt());
    }

    @Transactional(readOnly = true)
    public List<MilestoneResponseDto> getMileStoneListByProjectId(long projectId) {
        List<MileStone> mileStoneList = mileStoneRepository.getMileStoneByProjectId(projectId);

        return mileStoneList.stream()
                .map(ms ->
                        new MilestoneResponseDto(ms.getId(), ms.getTask().getId(), ms.getTitle(),
                                ms.getDescription(), ms.getStatus(), ms.getDueDate(), ms.getCreatedAt(),
                                ms.getUpdatedAt()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MilestoneListResponseDto> getMileStoneListByProjectIds(List<Long> projectIds) {
        List<MileStoneListProjection> mileStoneListProjects = mileStoneRepository.getMileStoneListByProjectIds(projectIds);

        Set<Long> setProjectIds = mileStoneListProjects.stream()
                .map(MileStoneListProjection::getProjectId)
                .collect(Collectors.toSet());

        List<MilestoneListResponseDto> responseDtoList = new ArrayList<>();
        for(Long projectId : setProjectIds) {
            List<MilestoneInfoResponseDto> milestoneResponseDtoList = mileStoneListProjects.stream()
                    .map(msl ->
                            new MilestoneInfoResponseDto(msl.getMileStoneId(), msl.getStatus()))
                    .toList();

            responseDtoList.add(new MilestoneListResponseDto(projectId, milestoneResponseDtoList));
        }

        return responseDtoList;
    }

    @Transactional
    public void createMileStone(Task task, MilestoneCreateRequestDto requestDto) {
        if(mileStoneRepository.existsMileStoneByTask_Id(task.getId())) {
            log.debug("[mile-stone service] 이미 존재하는 마일스톤 등록 시도입니다 - taskId;{}", task.getId());
            throw new MileStoneIsExistException("[mile-stone service] 이미 존재하는 마일스톤 등록 시도입니다");
        }

        MileStone mileStone = MileStone.create(task, requestDto);
        mileStoneRepository.save(mileStone);
    }

    @Transactional
    public void updateMileStone(long taskId, MilestoneUpdateRequestDto requestDto) {
        if(!mileStoneRepository.existsMileStoneByTask_Id(taskId)) {
            log.debug("[mile-ston service] 존재하지 않은 마일스톤 수정 요청입니다 - taskId:{}", taskId);
            throw new MileStoneIsNotExistException("[mile-ston service] 존재하지 않은 마일스톤 수정 요청입니다");
        }

        MileStone mileStone = mileStoneRepository.findMileStoneByTask_Id(taskId);

        if(!requestDto.title().equals(mileStone.getTitle())) {
            mileStone.setTitle(requestDto.title());
        }
        if(!requestDto.description().equals(mileStone.getDescription())) {
            mileStone.setDescription(requestDto.description());
        }
        if(!requestDto.status().equals(mileStone.getStatus())) {
            mileStone.setStatus(requestDto.status());
        }
        if(!requestDto.dueDate().equals(mileStone.getDueDate())) {
            mileStone.setDueDate(requestDto.dueDate());
        }
        mileStone.updateTime();

        mileStoneRepository.save(mileStone);
    }

    @Transactional
    public void deleteMileStone(long taskId) {
        if(!mileStoneRepository.existsMileStoneByTask_Id(taskId)) {
            log.debug("[mile-ston service] 존재하지 않은 마일스톤 삭제 요청입니다 - taskId:{}", taskId);
            throw new MileStoneIsNotExistException("[mile-ston service] 존재하지 않은 마일스톤 삭제 요청입니다");
        }

        mileStoneRepository.deleteMileStoneByTask_Id(taskId);
    }
}
