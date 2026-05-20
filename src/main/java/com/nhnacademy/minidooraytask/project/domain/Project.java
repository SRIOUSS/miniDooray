package com.nhnacademy.minidooraytask.project.domain;


import com.nhnacademy.minidooraytask.member.domain.ProjectMember;
import com.nhnacademy.minidooraytask.task.domain.Task;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "projects")
@Getter
@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "create_account_id", nullable = false)
    private Long createAccountId;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @OneToMany
    private List<ProjectMember> projectMemberList;

    @OneToMany
    private List<Task> taskList;

    //프로젝트를 처음 만들 때
    public Project(String title, String description, Long createAccountId) {
        this.title = title;
        this.description = description;
        this.createAccountId = createAccountId;
        this.status = ProjectStatus.ACTIVE;

        this.projectMemberList = new ArrayList<>();
        this.taskList = new ArrayList<>();
    }

    //존재하는 프로젝트 수정
    public void updateProjectInfo(String title, String description, ProjectStatus status) {

        if (title != null && !title.isBlank() && !this.title.equals(title)) {
            this.title = title;
        }
        if (description != null && !description.isBlank() && !this.description.equals(description)) {
            this.description = description;
        }
        if (status != null && !this.status.equals(status)) {
            this.status = status;
        }
    }

    public void isDelete() {
        this.isDeleted = true;
    }
}
