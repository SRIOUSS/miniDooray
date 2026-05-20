package com.nhnacademy.minidooraytask.task.service;


import com.nhnacademy.minidooraytask.member.repository.ProjectMemberRepository;
import com.nhnacademy.minidooraytask.member.service.ProjectMemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ProjectMemberService.class)
class ProjectMemberTest {

    @Autowired
    private ProjectMemberService projectMemberService;

    @MockitoBean
    private ProjectMemberRepository projectMemberRepository;

//
//    @Test
//    @DisplayName("프로젝트 member의 유무 확인 - ")
//    void get

}
