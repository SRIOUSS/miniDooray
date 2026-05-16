package controller;

import com.nhnacademy.minidooraytask.task.domain.TaskCreateRequestDto;
import com.nhnacademy.minidooraytask.task.domain.TaskResponseDto;
import com.nhnacademy.minidooraytask.task.domain.TaskUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/projects/{projectId}/tasks")
public class TaskController {

    @GetMapping
    public ResponseEntity<List<TaskResponseDto>> getTaskResponseDtoList(@PathVariable long projectId) {
        List<TaskResponseDto> requestDtoList = List.of();
        return ResponseEntity.ok().body(requestDtoList);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponseDto> getTaskResponseDto(@PathVariable long projectId,
                                                              @PathVariable long taskId) {
        TaskResponseDto responseDto = null;
        return ResponseEntity.ok().body(responseDto);
    }

    @PostMapping
    public ResponseEntity<Void> createTask(@PathVariable long projectId,
                                           @RequestBody TaskCreateRequestDto requestDto) {
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<Void> updateTask(@PathVariable long projectId,
                                           @PathVariable long taskId,
                                           @RequestBody TaskUpdateRequestDto requestDto) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable long projectId,
                                           @PathVariable long taskId) {
        return ResponseEntity.ok().build();
    }

}
