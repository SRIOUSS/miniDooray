package com.nhnacademy.minidooraytask.task.repository;

import com.nhnacademy.minidooraytask.task.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {

    //GET


}
