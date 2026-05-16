package controller;

import com.nhnacademy.minidooraytask.member.domain.ProjectMemberAddRequestDto;
import com.nhnacademy.minidooraytask.member.domain.ProjectMemberResponseDto;
import com.nhnacademy.minidooraytask.member.service.ProjectMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/projects/{projectId}/members")
public class ProjectMemberController {
    private final ProjectMemberService projectMemberService;

    @GetMapping
    public ResponseEntity<List<ProjectMemberResponseDto>> getProjectMemberResponseDtoList(@PathVariable long projectId) {
        List<ProjectMemberResponseDto> responseDtoList = projectMemberService.getProjectMembers(projectId);
        return ResponseEntity.ok().body(responseDtoList);
    }

    @PostMapping
    public ResponseEntity<Void> addProjectMember(@PathVariable long projectId,
                                                 @RequestBody ProjectMemberAddRequestDto requestDto) {
        projectMemberService.addProjectMember(projectId, requestDto);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> deleteProjectMember(@PathVariable long projectId,
                                                    @PathVariable long memberId) {
        projectMemberService.deleProjectMember(memberId);
    }
}
