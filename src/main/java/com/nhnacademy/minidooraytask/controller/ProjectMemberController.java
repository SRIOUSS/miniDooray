package com.nhnacademy.minidooraytask.controller;

import com.nhnacademy.minidooraytask.member.domain.*;
import com.nhnacademy.minidooraytask.member.service.ProjectMemberFacade;
import com.nhnacademy.minidooraytask.member.service.ProjectMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/task-api/projects/{projectId}/members")
public class ProjectMemberController {

    private final ProjectMemberFacade projectMemberFacade;
    private final ProjectMemberService projectMemberService;


    @GetMapping
    public ResponseEntity<MemberInfoListDto> getProjectMemberResponseDtoList(@PathVariable long projectId,
                                                                             @RequestHeader("X-Account-Id") long accountId) {
        MemberInfoListDto responseDtoList = projectMemberFacade.getMemberInfoList(projectId, accountId);
        return ResponseEntity.ok().body(responseDtoList);
    }

    // 멤버 추가
    @PostMapping
    public ResponseEntity<Void> addProjectMember(@PathVariable long projectId,
                                                 @RequestHeader("X-Account-Id") Long accountId,
                                                 @RequestBody MemberRequestDto requestDto) {

        projectMemberFacade.addProjectMember(projectId, accountId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //멤버 권한 변경
    @PutMapping("/{memberId}")
    public ResponseEntity<Void> updateProjectMember(@PathVariable long projectId,
                                                    @PathVariable long memberId,
                                                    @RequestHeader("X-Account-Id") Long accountId,
                                                    @RequestBody MemberRequestDto requestDto) {
        projectMemberFacade.updateMember(projectId, memberId, accountId, requestDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> deleteProjectMember(@PathVariable long projectId,
                                                    @PathVariable long memberId,
                                                    @RequestHeader("X-Account-Id") Long accountId) {
        projectMemberService.deleteProjectMember(projectId, memberId);
        return ResponseEntity.noContent().build();
    }
}
