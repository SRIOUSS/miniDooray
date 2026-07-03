package com.nhnacademy.minidooraytask.task.service;

import com.nhnacademy.minidooraytask.milestone.domain.MileStone;
import com.nhnacademy.minidooraytask.milestone.domain.MilestoneResponseDto;
import com.nhnacademy.minidooraytask.member.domain.ProjectMember;
import com.nhnacademy.minidooraytask.member.exception.ProjectMemberIsNotExistException;
import com.nhnacademy.minidooraytask.project.domain.Project;
import com.nhnacademy.minidooraytask.project.exception.NoAuthoProjectException;
import com.nhnacademy.minidooraytask.tag.domain.TagResponseDto;
import com.nhnacademy.minidooraytask.tag.domain.TaskTag;
import com.nhnacademy.minidooraytask.task.domain.Task;
import com.nhnacademy.minidooraytask.task.domain.TaskRequestDto;
import com.nhnacademy.minidooraytask.task.domain.TaskResponseDto;
import com.nhnacademy.minidooraytask.task.exception.TaskNotFoundException;
import com.nhnacademy.minidooraytask.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    // [특정 프로젝트 Task 목록 조회]
    @Transactional(readOnly = true)
    public List<Task> getTasks(Long projectId) {
        return taskRepository.findAllByProject_Id(projectId, false);
    }

    @Transactional
    public List<Task> getMytasks(long accountId) {
        return taskRepository.findByProjectMember_AccountId(accountId);
    }

    // [Task 단건 조회]
    @Transactional(readOnly = true)
    public Task getSpecificTask(Long taskId, Long projectId) {
        return taskRepository.findByIdAndProject_Id(taskId, projectId)
                .orElseThrow(() -> {
                    log.debug("[task service] 존재하지 않는 task입니다 - taskId:{}, projectId:{}", taskId, projectId);
                    return new TaskNotFoundException("[task service] 존재하지 않는 task입니다");
                });
    }

    // 태스크 작성자인지 확인 - 작성자가 아니면 403
    @Transactional(readOnly = true)
    public void checkTaskMaker(Long memberId, Long taskId) {
        if (!(taskRepository.existsByIdAndAccountId(taskId, memberId))) {
            log.debug("[task service] 태스크 수정/삭제 권한이 없습니다 - taskId:{}, memberId:{}", taskId, memberId);
            throw new NoAuthoProjectException("[task service] 태스크 수정/삭제 권한이 없습니다");
        }
    }

    @Transactional(readOnly = true)
    public void checkTaskMakerByAccountId(long accountId, long taskId) {
        if (!(taskRepository.existsTaskByProjectMember_AccountIdAndId(accountId, taskId))) {
            log.debug("[task service] 태스크 수정/삭제 권한이 없습니다 - taskId:{}, accountId:{}", taskId, accountId);
            throw new NoAuthoProjectException("[task service] 태스크 수정/삭제 권한이 없습니다");
        }
    }

    @Transactional(readOnly = true)
    public void checkProjectMember(long taskId, long accountId) {
        if (!taskRepository.existsByIdAndProject_ProjectMemberListIsAccountId(taskId, accountId)) {
            log.debug("[task service] 존재하지 않는 멤버입니다 - taskId:{}, accountId:{}", taskId, accountId);
            throw new ProjectMemberIsNotExistException("[task service] 프로젝트 멤버가 아닙니다");
        }
    }

    @Transactional(readOnly = true)
    public Task getTaskById(long taskId) {
        return Optional.ofNullable(taskRepository.findTaskById(taskId))
                .orElseThrow(() -> {
                    log.debug("[task service] 존재하지 않는 task입니다 - taskId:{}", taskId);
                    return new TaskNotFoundException("[task service] 존재하지 않는 task입니다. taskId: " + taskId);
                });
    }

    // [Task 생성]
    @Transactional
    public Task createTask(Project project, ProjectMember projectMember, TaskRequestDto request) {
        Task task = new Task(project, projectMember, request.title(), request.content());
        Task savedTask = taskRepository.save(task);
        log.debug("[task service] task 생성 완료 - taskId:{}, projectId:{}", savedTask.getId(), project.getId());

        projectMember.getTaskList().add(savedTask);
        project.getTaskList().add(savedTask);

        return savedTask;
    }

    // [Task 수정]
    @Transactional
    public Task updateTask(Task task, TaskRequestDto taskRequestDto) {
        task.updateTask(taskRequestDto.title(), taskRequestDto.content());
        taskRepository.save(task);
        return task;
    }

    // [Task 삭제]
    @Transactional
    public void deleteTask(Task task) {
        log.debug("[task service] task 삭제 완료 - taskId:{}", task.getId());
        task.isDelete();
        taskRepository.save(task);
    }

    // [TaskResponseDto 조립]
    public TaskResponseDto buildTaskResponseDto(Task task) {
        MileStone mileStone = task.getMilestone();
        MilestoneResponseDto milestoneResponseDto = null;
        if (mileStone != null) {
            milestoneResponseDto = new MilestoneResponseDto(
                    mileStone.getId(), mileStone.getTitle(), mileStone.getDescription(),
                    mileStone.getStatus(), mileStone.getDueDate(), mileStone.getCreatedAt(), mileStone.getUpdatedAt()
            );
        }

        List<TagResponseDto> tagResponseDtoList = task.getTaskTagList().stream()
                .map(TaskTag::getTag)
                .map(t -> new TagResponseDto(t.getId(), t.getName()))
                .toList();

        return new TaskResponseDto(
                task.getId(),
                task.getProject().getId(),
                task.getProjectMember().getId(),
                task.getTitle(),
                task.getContent(),
                task.getCreatedAt(),
                task.getUpdatedAt(),
                milestoneResponseDto,
                tagResponseDtoList
        );
    }
}