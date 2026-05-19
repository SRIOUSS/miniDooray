package controller;

import com.nhnacademy.minidooraytask.comment.domain.CommentListDto;
import com.nhnacademy.minidooraytask.comment.domain.CommentRequestDto;
import com.nhnacademy.minidooraytask.comment.domain.CommentResponseDto;
import com.nhnacademy.minidooraytask.comment.repository.CommentRepository;
import com.nhnacademy.minidooraytask.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/projects/{projectId}/tasks/{taskId}/comments")
public class CommentController {

    private final CommentService commentService;

    //마이페이지에서 내가 작성한 Comment 목록 (GET)
    @GetMapping("/mypage/comments")
    public ResponseEntity<CommentListDto> getCommentResponseDtoList(@RequestHeader("X-Account-Id") Long accountId) {
        CommentListDto commentListDto = commentService.getCommentsByAccountId(accountId);
        return ResponseEntity.ok().body(commentListDto);
    }

    //댓글 생성 (POST)
    @PostMapping
    public ResponseEntity<Void> createComment(@RequestHeader("X-Account-Id") Long accountId) {
        return ResponseEntity.ok().build();
    }

    //댓글 수정 (POST)
    @PostMapping("/{commentId}")
    public ResponseEntity<Void> updateComment(@PathVariable long projectId,
                                              @PathVariable long taskId,
                                              @PathVariable long commentId,
                                              @RequestHeader("X-Account-Id") Long accountId) {
        return ResponseEntity.ok().build();
    }

    //상황 : 댓글 삭제 (DELETE)
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable long projectId,
                                              @PathVariable long commentId,
                                              @RequestHeader("X-Account-Id") Long accountId) {
        return ResponseEntity.ok().build();
    }
}
