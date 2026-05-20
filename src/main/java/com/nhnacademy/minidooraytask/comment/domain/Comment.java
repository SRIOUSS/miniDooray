package com.nhnacademy.minidooraytask.comment.domain;

import com.nhnacademy.minidooraytask.member.domain.ProjectMember;
import com.nhnacademy.minidooraytask.project.domain.ProjectRequestDto;
import com.nhnacademy.minidooraytask.task.domain.Task;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private ProjectMember projectMember;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Comment(Task task, ProjectMember projectMember, String content) {
        this.task = task;
        this.projectMember = projectMember;
        this.content = content;

        this.createdAt = LocalDateTime.now();
    }

    public static Comment create(Task task, ProjectMember projectMember, CommentRequestDto requestDto) {
        return new Comment(task, projectMember, requestDto.content());
    }

    //댓글 수정
    public void updateContent(String content) {
        this.content = content;

        this.updatedAt = LocalDateTime.now();
    }
}
