package com.nhnacademy.minidooraytask.tag.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TagResponseDto {

    private Long tagId;
    private String name;

    public static TagResponseDto from(Tag tag) {
        return new TagResponseDto(
                tag.getId(),
                tag.getName()
        );
    }
}