package com.nhnacademy.minidooraytask.tag.domain;

import com.nhnacademy.minidooraytask.task.domain.Task;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Table(name = "taskTags")
public class TaskTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag tag;

    //생성자
    public TaskTag(Task task, Tag tag) {
        this.task = task;
        this.tag = tag;
    }
}