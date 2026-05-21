package com.nhnacademy.minidooraytask.controller;

import com.nhnacademy.minidooraytask.comment.domain.CommentRequestDto;
import com.nhnacademy.minidooraytask.comment.service.CommentFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/task-api/tasks/{taskId}/comments")
public class CommentController {

    private final CommentFacade commentFacade;

    //댓글 생성 (POST)
    @PostMapping
    public ResponseEntity<Void> createComment(@PathVariable long taskId,
                                              @RequestHeader("X-Account-Id") Long accountId,
                                              @RequestBody CommentRequestDto requestDto) {
        commentFacade.createComment(taskId, accountId, requestDto);
        return ResponseEntity.ok().build();
    }

    //댓글 수정 (POST)
    @PutMapping("/{commentId}")
    public ResponseEntity<Void> updateComment(@PathVariable long taskId,
                                              @PathVariable long commentId,
                                              @RequestHeader("X-Account-Id") Long accountId,
                                              @RequestBody CommentRequestDto requestDto) {
        commentFacade.updateComment(taskId, commentId, accountId, requestDto);
        return ResponseEntity.ok().build();
    }

    //상황 : 댓글 삭제 (DELETE)
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable long taskId,
                                              @PathVariable long commentId,
                                              @RequestHeader("X-Account-Id") Long accountId) {
        commentFacade.deleteComment(taskId, commentId, accountId);
        return ResponseEntity.ok().build();
    }
}
