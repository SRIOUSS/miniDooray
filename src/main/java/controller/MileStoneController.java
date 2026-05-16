package controller;

import com.nhnacademy.minidooraytask.MileStone.domain.MilestoneResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/projects/{projectId}/tasks/{taskId}/milestone")
public class MileStoneController {

    @GetMapping
    public ResponseEntity<MilestoneResponseDto> getMilestoneResponseDtoList(@PathVariable long projectId,
                                                                            @PathVariable long taskId) {
        MilestoneResponseDto response = null;
        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public ResponseEntity<Void> createMileStone(@PathVariable)
}
