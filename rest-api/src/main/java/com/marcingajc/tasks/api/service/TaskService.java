package com.marcingajc.tasks.api.service;

import com.marcingajc.tasks.api.domain.Task;
import com.marcingajc.tasks.api.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository repository;

    public List<Task> getAllTasks() {
        return repository.findAll();
    }

    public Task getTask(Long id) {
        return repository.getOne(id);
    }

    public Task saveTask(final Task task) {

        if (task.getStartDate() == null) {
            task.setStartDate(dateFormatter().format(LocalDateTime.now()));
        }
        return repository.save(task);
    }

    public Task saveTaskWithId(Long id, Task task) {

        task.setId(id);

        if (task.getStartDate() == null) {
            task.setStartDate(repository.getOne(id).getStartDate());
        }

        if (task.getUpdatedDate() == null) {
            task.setUpdatedDate(dateFormatter().format(LocalDateTime.now()));
        }

        if (task.isCompleted()) {
            task.setEndDate(dateFormatter().format(LocalDateTime.now()));
        }

        return repository.save(task);
    }

    public void deleteTask(Long id) {

        repository.deleteById(id);
    }

    public void deleteAllTask() {
        repository.deleteAllInBatch();
    }

    private DateTimeFormatter dateFormatter() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    }

}
