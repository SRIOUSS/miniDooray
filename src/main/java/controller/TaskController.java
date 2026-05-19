package controller;

import com.nhnacademy.minidooraytask.task.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/projects/{projectId}/tasks")
public class TaskController {

    @GetMapping
    public ResponseEntity<TaskInfoListDto> getTaskResponseDtoList(@PathVariable long projectId,
                                                                  @RequestHeader("X-Account-Id") Long accountId) {
        TaskInfoListDto responseDto = null;
        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskViewDto> getTaskResponseDto(@PathVariable long projectId,
                                                          @PathVariable long taskId,
                                                          @RequestHeader("X-Account-Id") Long accountId) {
        TaskViewDto responseDto = null;
        return ResponseEntity.ok().body(responseDto);
    }

    @PostMapping
    public ResponseEntity<Void> createTask(@PathVariable long projectId,
                                           @RequestHeader("X-Account-Id") Long accountId,
                                           @RequestBody TaskRequestDto requestDto) {
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<Void> updateTask(@PathVariable long projectId,
                                           @PathVariable long taskId,
                                           @RequestHeader("X-Account-Id") Long accountId,
                                           @RequestBody TaskRequestDto requestDto) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable long projectId,
                                           @PathVariable long taskId,
                                           @RequestHeader("X-Account-Id") Long accountId) {
        return ResponseEntity.ok().build();
    }

}
