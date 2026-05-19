package com.nhnacademy.minidooraytask.task.service;

import com.nhnacademy.minidooraytask.MileStone.domain.MilestoneResponseDto;
import com.nhnacademy.minidooraytask.MileStone.repository.MileStoneRepository;
import com.nhnacademy.minidooraytask.member.domain.ProjectMember;
import com.nhnacademy.minidooraytask.member.exception.ProjectMemberIsNotExistException;
import com.nhnacademy.minidooraytask.member.repository.ProjectMemberRepository;
import com.nhnacademy.minidooraytask.project.domain.Project;
import com.nhnacademy.minidooraytask.project.respository.ProjectRepository;
import com.nhnacademy.minidooraytask.tag.domain.TagResponseDto;
import com.nhnacademy.minidooraytask.tag.repository.TagRepository;
import com.nhnacademy.minidooraytask.tag.repository.TaskTagRepository;
import com.nhnacademy.minidooraytask.task.domain.Task;
import com.nhnacademy.minidooraytask.task.domain.TaskRequestDto;
import com.nhnacademy.minidooraytask.task.domain.TaskResponseDto;
import com.nhnacademy.minidooraytask.task.domain.TaskUpdateRequestDto;
import com.nhnacademy.minidooraytask.task.exception.TaskNotFoundException;
import com.nhnacademy.minidooraytask.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskTagRepository taskTagRepository;
    private final TagRepository tagRepository;
    private final MileStoneRepository milestoneRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;

    // [특정 프로젝트 Task 목록 조회]
    @Transactional(readOnly = true)
    public List<Task> getTasks(Long projectId) {
        return taskRepository.findAllByProject_Id(projectId);
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

    //테스크 작성자인지 확인
    @Transactional(readOnly = true)
    public void checkTaskMaker(Long memberId, Long taskId) {
        if(taskRepository.findByIdAndProject_Id(taskId,memberId).isEmpty()) {
            log.debug("[task service] 해당 테스크의 작성자가 아닙니다 - taskId:{}, memberId:{}", taskId, memberId);
            throw new TaskNotFoundException("[task service] 존재하지 않는 task입니다");
        }
    }

    @Transactional(readOnly = true)
    public Task getTaskById(long taskId) {
        return taskRepository.findTaskById(taskId);
    }


    // [Task 생성]
    @Transactional
    public void createTask(Project project, ProjectMember projectMember, TaskRequestDto request) {

        Task task = new Task(project, projectMember, request.title(), request.content());
        Task savedTask = taskRepository.save(task);
        log.debug("[task service] task 생성 완료 - taskId:{}, projectId:{}", savedTask.getId(), project.getId());
    }

    // [Task 수정]
    @Transactional
    public Task updateTask(Long projectId, Long taskId, Long accountId, TaskUpdateRequestDto request) {


//       Task task =  updateTask(projectId,taskId, accountId, request);
//        log.debug("[task service] task 수정 완료 - taskId:{}", taskId);
//
//        taskTagRepository.deleteAllByTask_Id(taskId);
//
//        if (request.tagIds() != null && !request.tagIds().isEmpty()) {
//            List<Tag> tags = tagRepository.findAllById(request.tagIds());
//            if (tags.size() != request.tagIds().size()) {
//                log.debug("[task service] 존재하지 않는 태그가 포함되어 있습니다 - tagIds:{}", request.tagIds());
//                throw new TagIsNotExistException("[task service] 존재하지 않는 태그가 포함되어 있습니다");
//            }
//            List<TaskTag> taskTags = tags.stream()
//                    .map(tag -> new TaskTag(task, tag))
//                    .collect(Collectors.toList());
//            taskTagRepository.saveAll(taskTags);
//        }
        return null;
    }

    // [Task 삭제]
    @Transactional
    public void deleteTask(Long projectId, Long taskId, Long accountId) {

        projectMemberRepository.findByProject_IdAndAccountId(projectId, accountId)
                .filter(member -> !member.isDeleted())
                .orElseThrow(() -> {
                    log.debug("[task service] 삭제 권한이 없는 멤버입니다 - projectId:{}, accountId:{}", projectId, accountId);
                    return new ProjectMemberIsNotExistException("[task service] 삭제 권한이 없는 멤버입니다");
                });

        Task task = taskRepository.findByIdAndProject_Id(taskId, projectId)
                .orElseThrow(() -> {
                    log.debug("[task service] 존재하지 않는 task입니다 - taskId:{}, projectId:{}", taskId, projectId);
                    return new TaskNotFoundException("[task service] 존재하지 않는 task입니다");
                });

        taskTagRepository.deleteAllByTask_Id(taskId);

        taskRepository.delete(task);
        log.debug("[task service] task 삭제 완료 - taskId:{}", taskId);
    }

    // [TaskResponseDto 조립 메서드]
    private TaskResponseDto buildTaskResponseDto(Task task) {

//        Milestone milestone = milestoneRepository.findMileStoneByTask_Id(task.getId());
        MilestoneResponseDto milestoneResponseDto = null;

//        if (milestone != null) {
//            milestoneResponseDto = new MilestoneResponseDto(
//                    milestone.getId(),
//                    milestone.getTask().getId(),
//                    milestone.getTitle(),
//                    milestone.getDescription(),
//                    milestone.getStatus(),
//                    milestone.getDueDate(),
//                    milestone.getCreatedAt(),
//                    milestone.getUpdatedAt()
//            );
//        }

        List<TagResponseDto> tagResponseDtos = taskTagRepository.findAllByTask_Id(task.getId())
                .stream()
                .map(taskTag -> new TagResponseDto(
                        taskTag.getTag().getId(),
                        taskTag.getTag().getName()
                ))
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
                tagResponseDtos
        );
    }


}
