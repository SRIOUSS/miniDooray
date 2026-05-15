package com.nhnacademy.minidooraytask.member.domain;

import com.nhnacademy.minidooraytask.project.domain.Project;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ProjectMemberResponseDto {

    private Long projectMemberId;
    private Project project; //(project_id)
    private Long accountId;
    private MembersAuth auth;
    private LocalDateTime joinedAt;

    public static ProjectMemberResponseDto from(ProjectMember projectMember) {

        return new ProjectMemberResponseDto(
                projectMember.getId(),
                projectMember.getProject(),
                projectMember.getAccountId(),
                projectMember.getAuth(),
                projectMember.getJoinedAt()
        );
    }
}
