package com.nhnacademy.minidooraytask.comment.service;

import com.nhnacademy.minidooraytask.comment.domain.Comment;
import com.nhnacademy.minidooraytask.comment.domain.CommentRequestDto;
import com.nhnacademy.minidooraytask.comment.domain.CommentResponseDto;
import com.nhnacademy.minidooraytask.comment.exception.CommentNotAuthorizedException;
import com.nhnacademy.minidooraytask.comment.exception.CommentNotFoundException;
import com.nhnacademy.minidooraytask.comment.repository.CommentRepository;
import com.nhnacademy.minidooraytask.member.domain.ProjectMember;
import com.nhnacademy.minidooraytask.member.exception.ProjectMemberIsNotExistException;
import com.nhnacademy.minidooraytask.member.repository.ProjectMemberRepository;
import com.nhnacademy.minidooraytask.project.exception.ProjectNotFoundException;
import com.nhnacademy.minidooraytask.project.respository.ProjectRepository;
import com.nhnacademy.minidooraytask.task.domain.Task;
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
public class CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectRepository projectRepository;

    @Transactional(readOnly = true)
    public List<CommentResponseDto> getComments(Long projectId, Long taskId) {

        //해당 프로젝트 존재 확인
        projectRepository.findByProjectId(projectId)
                .orElseThrow(() ->{
                    log.debug("[comment service] 존재하지 않는 프로젝트 입니다 - projectId : {}",projectId);
                    return new ProjectNotFoundException("[comment service] 존재하지 않는 프로젝트 입니다" );
                });

        return commentRepository.findAllByTask_Id(taskId)
                .stream()
                .map(comment -> new CommentResponseDto(
                        comment.getId(),
                        comment.getTask().getId(),
                        comment.getProjectMember().getId(),
                        comment.getContent(),
                        comment.getCreatedAt(),
                        comment.getUpdatedAt()
                ))
                .toList();
    }

    //[댓글 생성]
    @Transactional
    public CommentResponseDto createComment(long projectId, long taskId, long accountId, CommentRequestDto requestDto) {

        // task 조회
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> {
                    log.debug("[comment service] 존재하지 않는 task입니다 - taskId:{}", taskId);
                    return new TaskNotFoundException("[comment service] 존재하지 않는 task입니다");
                });

        ProjectMember projectMember = projectMemberRepository.findByProject_IdAndAccountId(projectId, accountId)
                .orElseThrow(() -> {
                    log.debug("[comment service] 존재하지 않는 멤버입니다 - projectId:{}, accountId:{}", projectId, accountId);
                    return new ProjectMemberIsNotExistException("[comment service] 존재하지 않는 멤버입니다");
                });

        Comment comment = new Comment(null, task, projectMember, requestDto.content(), null, null);

        Comment saveC = commentRepository.save(comment);
        log.debug("[comment service] 댓글 생성 완료 - taskId : {}, memberId : {}",
                saveC.getTask().getId(), saveC.getProjectMember().getId());

        return new CommentResponseDto(
                saveC.getId(),
                saveC.getTask().getId(),
                saveC.getProjectMember().getId(),
                saveC.getContent(),
                saveC.getCreatedAt(),
                saveC.getUpdatedAt()
        );
    }

    //[댓글 수정]
    @Transactional
    public void updateComment(Long projectId, Long taskId, Long commentId, Long accountId, CommentRequestDto requestDto) {

        // accountId로 ProjectMember 먼저 조회
        ProjectMember projectMember = projectMemberRepository.findByProject_IdAndAccountId(projectId, accountId)
                .orElseThrow(() -> {
                    log.debug("[comment service] 존재하지 않는 멤버 수정입니다 - projectId:{}, accountId:{}", projectId, accountId);
                    return new ProjectMemberIsNotExistException("[comment service] 존재하지 않는 멤버 수정입니다");
                });

        // 댓글 존재 여부 확인
        commentRepository.findByIdAndTask_Id(commentId, taskId)
                .orElseThrow(() -> {
                    log.debug("[comment service] 존재하지 않는 댓글 수정입니다 - commentId:{}, taskId:{}", commentId, taskId);
                    return new CommentNotFoundException("[comment service] 존재하지 않는 댓글입니다");
                });

        // projectMember.getId()로 본인 댓글인지 확인
        Comment comment = commentRepository.findByIdAndTask_IdAndProjectMember_Id(commentId, taskId, projectMember.getId())
                .orElseThrow(() -> {
                    log.debug("[comment service] 댓글 수정 권한이 없습니다 - commentId:{}, memberId:{}", commentId, projectMember.getId());
                    return new CommentNotAuthorizedException("[comment service] 댓글 수정 권한이 없습니다");
                });

        comment.updateContent(requestDto.content());
    }

    //댓글 삭제
    @Transactional
    public void deleteComment(Long projectId, Long taskId, Long commentId, Long accountId) {

        // accountId로 ProjectMember 조회
        ProjectMember projectMember = projectMemberRepository.findByProject_IdAndAccountId(projectId, accountId)
                .orElseThrow(() -> {
                    log.debug("[comment service] 존재하지 않는 멤버 삭제입니다 - projectId:{}, accountId:{}", projectId, accountId);
                    return new ProjectMemberIsNotExistException("[comment service] 존재하지 않는 멤버입니다");
                });

        commentRepository.findByIdAndTask_Id(commentId, taskId)
                .orElseThrow(() -> {
                    log.debug("[comment service] 존재하지 않는 댓글입니다 - commentId:{}, taskId:{}", commentId, taskId);
                    return new CommentNotFoundException("[comment service] 존재하지 않는 댓글입니다");
                });

        // 본인 댓글인지 확인 - memberId 대신 projectMember.getId()
        Comment comment = commentRepository.findByIdAndTask_IdAndProjectMember_Id(commentId, taskId, projectMember.getId())
                .orElseThrow(() -> {
                    log.debug("[comment service] 댓글 삭제 권한이 없습니다 - commentId:{}, memberId:{}", commentId, projectMember.getId());
                    return new CommentNotAuthorizedException("[comment service] 댓글 삭제 권한이 없습니다");
                });

        commentRepository.delete(comment);
        log.debug("[comment service] 댓글 삭제 완료 - commentId:{}, memberId:{}", commentId, projectMember.getId());
    }
}
