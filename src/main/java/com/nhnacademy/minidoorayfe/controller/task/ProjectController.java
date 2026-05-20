package com.nhnacademy.minidoorayfe.controller.task;

import com.nhnacademy.minidoorayfe.resolver.SessionIdentity;
import com.nhnacademy.minidoorayfe.api.TaskApiClient;
import com.nhnacademy.minidoorayfe.dto.auth.SessionAccountDto;
import com.nhnacademy.minidoorayfe.dto.member.MemberRequestDto;
import com.nhnacademy.minidoorayfe.dto.project.ProjectRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/projects")
public class ProjectController {

    private final TaskApiClient taskApiClient;

    // 프로젝트 목록
    @GetMapping
    public String getProjects(@SessionIdentity SessionAccountDto sessionAccountDto,
                              Model model) {

        model.addAttribute("projectView", this.taskApiClient.getProjects(sessionAccountDto.getAccountId()));

        return "project/list";
    }

    // 프로젝트 생성 폼
    @GetMapping("/new")
    public String createProjectForm(Model model) {

        model.addAttribute("projectRequestDto", new ProjectRequestDto());

        return "project/form";
    }

    // 프로젝트 생성
    @PostMapping
    public String createProject(@SessionIdentity SessionAccountDto sessionAccountDto,
                                @ModelAttribute ProjectRequestDto dto) {

        this.taskApiClient.createProject(dto, sessionAccountDto.getAccountId());

        return "redirect:/projects";
    }

    // 프로젝트 상세
    @GetMapping("/{projectId}")
    public String getProject(@SessionIdentity SessionAccountDto sessionAccountDto,
                             @PathVariable Long projectId,
                             Model model) {

        model.addAttribute("projectId", projectId);
        model.addAttribute("tasks", this.taskApiClient.getTasks(projectId, sessionAccountDto.getAccountId()));
        model.addAttribute("members", this.taskApiClient.getMembers(projectId, sessionAccountDto.getAccountId()));

        return "project/detail";
    }

    // 프로젝트 수정 폼
    @GetMapping("/{projectId}/edit")
    public String updateProjectForm(@SessionIdentity SessionAccountDto sessionAccountDto,
                                    @PathVariable Long projectId,
                                    Model model) {

        model.addAttribute("projectId", projectId);
        model.addAttribute("tasks", this.taskApiClient.getTasks(projectId, sessionAccountDto.getAccountId()));
        model.addAttribute("projectRequestDto", new ProjectRequestDto());

        return "project/edit";
    }

    // 프로젝트 수정
    @PutMapping("/{projectId}")
    public String updateProject(@SessionIdentity SessionAccountDto sessionAccountDto,
                                @PathVariable Long projectId,
                                @ModelAttribute ProjectRequestDto dto) {

        this.taskApiClient.updateProject(projectId, dto, sessionAccountDto.getAccountId());

        return "redirect:/projects/{projectId}";
    }

    // 프로젝트 삭제
    @DeleteMapping("/{projectId}")
    public String deleteProject(@SessionIdentity SessionAccountDto sessionAccountDto,
                                @PathVariable Long projectId) {

        this.taskApiClient.deleteProject(projectId, sessionAccountDto.getAccountId());

        return "redirect:/projects";
    }

    // 멤버 추가 폼
    @GetMapping("/{projectId}/members/new")
    public String addMemberForm(@PathVariable Long projectId,
                                Model model) {

        model.addAttribute("projectId", projectId);
        model.addAttribute("memberRequestDto", new MemberRequestDto());

        return "project/member-form";
    }

    // 멤버 추가
    @PostMapping("/{projectId}/members")
    public String addMember(@SessionIdentity SessionAccountDto sessionAccountDto,
                            @PathVariable Long projectId,
                            @ModelAttribute MemberRequestDto dto) {

        this.taskApiClient.addMember(projectId, sessionAccountDto.getAccountId(), dto);

        return "redirect:/projects/{projectId}";
    }

    // 멤버 권한 변경
    @PutMapping("/{projectId}/members/{memberId}")
    public String updateMemberAuth(@SessionIdentity SessionAccountDto sessionAccountDto,
                                   @PathVariable Long projectId,
                                   @PathVariable Long memberId,
                                   @ModelAttribute MemberRequestDto dto) {

        this.taskApiClient.updateMemberAuth(projectId, memberId, dto, sessionAccountDto.getAccountId());

        return "redirect:/projects/{projectId}";
    }

    // 멤버 삭제
    @DeleteMapping("/{projectId}/members/{memberId}")
    public String deleteMember(@SessionIdentity SessionAccountDto sessionAccountDto,
                               @PathVariable Long projectId,
                               @PathVariable Long memberId) {

        this.taskApiClient.deleteMember(projectId, sessionAccountDto.getAccountId(), memberId);

        return "redirect:/projects/{projectId}";
    }
}