package com.nhnacademy.minidooraytask.MileStone.domain;

import com.nhnacademy.minidooraytask.MileStone.exception.MileStoneInvalidInputException;
import com.nhnacademy.minidooraytask.task.domain.Task;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "milestones", uniqueConstraints = {@UniqueConstraint(columnNames = {"task_id"})})
public class MileStone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @Column(nullable = false, length = 30)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    private MileStoneStatus status;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public MileStone(Task task, String title, String description, MileStoneStatus status, LocalDateTime dueDate) {
        this.task = task;
        this.title = title;
        this.description = description;
        this.status = status;
        this.dueDate = dueDate;
        this.createdAt = LocalDateTime.now();
    }

    public static MileStone create(Task task, MilestoneRequestDto requestDto) {
        return new MileStone(task, requestDto.title(), requestDto.description(),
                requestDto.status(), requestDto.dueDate());
    }

    public void setDueDate(LocalDateTime dueDate) {
        if(Objects.isNull(dueDate)) {
            throw new MileStoneInvalidInputException("dueDate 입력 null");
        }
        this.dueDate = dueDate;
    }

    public void setStatus(MileStoneStatus status) {
        if(Objects.isNull(status)) {
            throw new MileStoneInvalidInputException("status 입력 null");
        }
        this.status = status;
    }

    public void setDescription(String description) {
        if(Objects.isNull(description) || description.isBlank()) {
            throw new MileStoneInvalidInputException("description 입력 null");
        }
        this.description = description;
    }

    public void setTitle(String title) {
        if(Objects.isNull(title) || title.isBlank()) {
            throw new MileStoneInvalidInputException("title 입력 null");
        }
        this.title = title;
    }

    public void updateTime() {
        this.updatedAt = LocalDateTime.now();
    }
}
