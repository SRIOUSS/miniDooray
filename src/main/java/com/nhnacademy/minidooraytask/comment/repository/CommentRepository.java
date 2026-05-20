package com.nhnacademy.minidooraytask.comment.repository;

import com.nhnacademy.minidooraytask.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    //GET
    List<Comment> findByProjectMember_AccountId(Long accountId);

    //댓글이 해당 task에 속하는지 확인
    Optional<Comment> findByIdAndTask_Id(Long id, Long taskId);

    //수정,삭제 시 본인의 뎃글인지 검증
    Optional<Comment> findByIdAndTask_IdAndProjectMember_Id(Long id, Long taskId, Long memberId);

    @Query("SELECT 1 FROM Comment c JOIN Task t ON c.task.id = t.id JOIN ProjectMember pm ON c.projectMember.id = pm.id WHERE t.id = ?1 AND c.id = ?2 AND pm.accountId = ?3")
    boolean existProjectMemberByTaskIdAndCommentIdANdAccountId(Long taskId, Long commentId, Long accountId);

    <T> Comment findCommentById(Long id);

    List<Comment> findAllByProjectMember_AccountId(Long projectMemberAccountId);
}
