package com.nhnacademy.minidooraytask.member.domain;

import com.nhnacademy.minidooraytask.comment.domain.Comment;
import com.nhnacademy.minidooraytask.member.exception.ProjectMemberInvalidException;
import com.nhnacademy.minidooraytask.project.domain.Project;
import com.nhnacademy.minidooraytask.task.domain.Task;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @OneToMany
    private List<Task> taskList;

    @OneToMany
    private List<Comment> commentList;

    public ProjectMember(Project project, Long accountId, MembersAuth auth) {
        this.project = project;
        this.accountId = accountId;
        this.auth = auth;
        this.isDeleted = false;

        this.joinedAt = LocalDateTime.now();
        this.taskList = new ArrayList<>();
        this.commentList = new ArrayList<>();
    }

    // 소프트 삭제 메서드
    public void delete() {
        this.isDeleted = true;
    }

    //생성자
    public ProjectMember(Project project, Long accountId) {
        this(project, accountId, MembersAuth.MEMBER);
    }

    //삭제 복구
    public void restore() {
        this.isDeleted = false;
    }

    public void setAuth(MembersAuth auth) {
        if(Objects.isNull(auth)) {
            throw new ProjectMemberInvalidException("[ProjectMember] 잘못된 auth 입력");
        }
        this.auth = auth;
    }
}
