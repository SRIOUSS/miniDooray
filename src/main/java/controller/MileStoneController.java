package controller;

import com.nhnacademy.minidooraytask.MileStone.domain.MilestoneCreateRequestDto;
import com.nhnacademy.minidooraytask.MileStone.domain.MilestoneResponseDto;
import com.nhnacademy.minidooraytask.MileStone.domain.MilestoneUpdateRequestDto;
import com.nhnacademy.minidooraytask.MileStone.service.MileStoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/projects/{projectId}/tasks/{taskId}/milestone")
public class MileStoneController {
    private final MileStoneService mileStoneService;

    @GetMapping
    public ResponseEntity<MilestoneResponseDto> getMilestoneResponseDtoList(@PathVariable long projectId,
                                                                            @PathVariable long taskId) {
        MilestoneResponseDto response = mileStoneService.getMileStoneByTaskId(taskId);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public ResponseEntity<Void> createMileStone(@PathVariable long projectId,
                                                @PathVariable long taskId,
                                                @RequestBody MilestoneCreateRequestDto requestDto) {
        mileStoneService.createMileStone(null, requestDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Void> updateMileStone(@PathVariable long projectId,
                                                @PathVariable long taskId,
                                                @RequestBody MilestoneUpdateRequestDto requestDto) {
        mileStoneService.updateMileStone(taskId, requestDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteMileStone(@PathVariable long projectId,
                                                @PathVariable long taskId) {
        mileStoneService.deleteMileStone(taskId);
        return ResponseEntity.ok().build();
    }
}
