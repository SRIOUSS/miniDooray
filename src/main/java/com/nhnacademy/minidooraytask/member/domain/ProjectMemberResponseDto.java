package com.nhnacademy.minidooraytask.member.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ProjectMemberResponseDto {

    private Long projectMemberId;
    private Long projectId; // projectId만
    private Long accountId;
    private MembersAuth auth;
    private LocalDateTime joinedAt;

    public static ProjectMemberResponseDto from(ProjectMember projectMember) {
        return new ProjectMemberResponseDto(
                projectMember.getId(),
                projectMember.getProject().getId(), // 엔티티에서 Id만 꺼내기
                projectMember.getAccountId(),
                projectMember.getAuth(),
                projectMember.getJoinedAt()
        );
    }
}
