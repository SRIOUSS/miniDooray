package com.nhnacademy.minidooraytask.tag.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "tagResponseDtoList")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String name;

    @OneToMany
    private List<TaskTag> taskTagList;

    public Tag(String name) {
        this.name = name;
        this.taskTagList = new ArrayList<>();
    }

    public void update(String name) {
        this.name = name;
    }
}
