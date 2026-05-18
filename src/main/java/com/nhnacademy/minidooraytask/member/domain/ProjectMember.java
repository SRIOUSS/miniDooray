package com.nhnacademy.minidooraytask.member.domain;

import com.nhnacademy.minidooraytask.project.domain.Project;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Getter
@Entity
@Table(name = "members")
public class ProjectMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;


    @Column(name = "account_id", nullable = false)
    private Long accountId;

    @Enumerated(EnumType.STRING)
    private MembersAuth auth;

    @CreatedDate
    @Column(name = "joined_at", nullable = false, updatable = false)
    private LocalDateTime joinedAt;

    //삭제
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    // 소프트 삭제 메서드
    public void delete() {
        this.isDeleted = true;
    }

    //생성자
    public ProjectMember(Project project, Long accountId) {
        this.project = project;
        this.accountId = accountId;
        this.auth = MembersAuth.MEMBER;
        this.isDeleted = false;
    }
}
