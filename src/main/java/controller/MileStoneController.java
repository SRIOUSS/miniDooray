package controller;

import com.nhnacademy.minidooraytask.MileStone.domain.MilestoneRequestDto;
import com.nhnacademy.minidooraytask.MileStone.domain.MilestoneUpdateRequestDto;
import com.nhnacademy.minidooraytask.MileStone.service.MileStoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/tasks/{taskId}/milestone")
public class MileStoneController {
    private final MileStoneService mileStoneService;

    @PostMapping
    public ResponseEntity<Void> createMileStone(@PathVariable long taskId,
                                                @RequestHeader("X-Account-Id") long accountId,
                                                @RequestBody MilestoneRequestDto requestDto) {
//        mileStoneService.createMileStone(null, requestDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Void> updateMileStone(@PathVariable long taskId,
                                                @RequestHeader("X-Account-Id") long accountId,
                                                @RequestBody MilestoneRequestDto requestDto) {
//        mileStoneService.updateMileStone(taskId, requestDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteMileStone(@PathVariable long taskId,
                                                @RequestHeader("X-Account-Id") long accountId) {
        mileStoneService.deleteMileStone(taskId);
        return ResponseEntity.ok().build();
    }
}
