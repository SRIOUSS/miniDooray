package com.nhnacademy.minidooraytask.project.domain;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

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

    //프로젝트를 처음 만들 때
    public Project(String title, String description, Long createAccountId) {
        this.title = title;
        this.description = description;
        this.createAccountId = createAccountId;
        this.status = ProjectStatus.ACTIVE;
    }

    //존재하는 프로젝트 수정
    public void updateProjectInfo(String title, String description, ProjectStatus status) {

        if (title != null) {
            this.title = title;
        }
        if (description != null) {
            this.description = description;
        }
        if (status != null) {
            this.status = status;
        }
    }
}
