package com.nhnacademy.minidooraytask.comment.repository;

import com.nhnacademy.minidooraytask.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    //GET
    List<Comment> findByProjectMember_AccountId(Long accountId);

    //댓글이 해당 task에 속하는지 확인
    Optional<Comment> findByIdAndTask_Id(Long id, Long taskId);

    //수정,삭제 시 본인의 뎃글인지 검증
    Optional<Comment> findByIdAndTask_IdAndProjectMember_Id(Long id, Long taskId, Long memberId);


}
