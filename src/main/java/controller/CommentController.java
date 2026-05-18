package controller;

import com.nhnacademy.minidooraytask.comment.domain.CommentRequestDto;
import com.nhnacademy.minidooraytask.comment.domain.CommentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/projects/{projectId}/tasks/{taskId}/comments")
public class CommentController {

    @GetMapping
    public ResponseEntity<List<CommentResponseDto>> getCommentResponseDtoList(@PathVariable long projectId,
                                                                              @PathVariable long taskId) {
        List<CommentResponseDto> responseDtoList = List.of();
        return ResponseEntity.ok().body(responseDtoList);
    }

    @PostMapping
    public ResponseEntity<Void> createComment(@PathVariable long projectId,
                                              @PathVariable long taskId,
                                              @RequestBody CommentRequestDto requestDto) {
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<Void> updateComment(@PathVariable long projectId,
                                              @PathVariable long taskId,
                                              @PathVariable long commentId,
                                              @RequestBody CommentRequestDto requestDto) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable long projectId,
                                              @PathVariable long taskId,
                                              @PathVariable long commentId) {
        return ResponseEntity.ok().build();
    }
}
