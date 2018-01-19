package com.marcingajc.tasks.api.repository;

import com.marcingajc.tasks.api.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
