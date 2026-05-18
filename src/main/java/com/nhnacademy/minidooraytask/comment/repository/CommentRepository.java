package com.nhnacademy.minidooraytask.comment.repository;

import com.nhnacademy.minidooraytask.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {


}
