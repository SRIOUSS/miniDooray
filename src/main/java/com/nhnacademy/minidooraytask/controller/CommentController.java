package com.nhnacademy.minidooraytask.controller;

import com.nhnacademy.minidooraytask.comment.domain.CommentListDto;
import com.nhnacademy.minidooraytask.comment.domain.CommentRequestDto;
import com.nhnacademy.minidooraytask.comment.service.CommentFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/task-api")
public class CommentController {

    private final CommentFacade commentFacade;

    //마이페이지에서 내가 작성한 Comment 목록 (GET)
    @GetMapping("/mypage/comments")
    public ResponseEntity<CommentListDto> getCommentResponseDtoList(@RequestHeader("X-Account-Id") Long accountId) {
        CommentListDto commentListDto = commentFacade.getCommentList(accountId);
        return ResponseEntity.ok().body(commentListDto);
    }

    //댓글 생성 (POST)
    @PostMapping("/tasks/{taskId}/comments")
    public ResponseEntity<Void> createComment(@PathVariable long taskId,
                                              @RequestHeader("X-Account-Id") Long accountId,
                                              @RequestBody CommentRequestDto requestDto) {
        commentFacade.createComment(taskId, accountId, requestDto);
        return ResponseEntity.ok().build();
    }

    //댓글 수정 (POST)
    @PostMapping("/tasks/{taskId}/comments/{commentId}")
    public ResponseEntity<Void> updateComment(@PathVariable long taskId,
                                              @PathVariable long commentId,
                                              @RequestHeader("X-Account-Id") Long accountId,
                                              @RequestBody CommentRequestDto requestDto) {
        commentFacade.updateComment(taskId, commentId, accountId, requestDto);
        return ResponseEntity.ok().build();
    }

    //상황 : 댓글 삭제 (DELETE)
    @DeleteMapping("/tasks/{taskId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable long taskId,
                                              @PathVariable long commentId,
                                              @RequestHeader("X-Account-Id") Long accountId) {
        commentFacade.deleteComment(taskId, commentId, accountId);
        return ResponseEntity.ok().build();
    }
}
