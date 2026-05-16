package controller;

import com.nhnacademy.minidooraytask.tag.domain.TagCreateRequestDto;
import com.nhnacademy.minidooraytask.tag.domain.TagResponseDto;
import com.nhnacademy.minidooraytask.tag.domain.TagUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/tags")
public class TagController {

    @GetMapping
    public ResponseEntity<List<TagResponseDto>> getTagResponseDtoList() {
        List<TagResponseDto> responseDtoList = List.of();
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Void> createTag(@RequestBody TagCreateRequestDto requestDto) {
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{tagId}")
    public ResponseEntity<Void> updateTag(@PathVariable long tagId, @RequestBody TagUpdateRequestDto requestDto) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{tagId}")
    public ResponseEntity<Void> deleteTag(@PathVariable long tagId) {
        return ResponseEntity.ok().build();
    }
}
