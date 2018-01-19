package com.marcingajc.tasks.api.mapper;

import com.marcingajc.tasks.api.domain.Task;
import com.marcingajc.tasks.api.domain.TaskDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TaskMapper {

    public Task mapToTask (final TaskDto taskDto) {
        return new Task (
                taskDto.getTitle(),
                taskDto.getDescription(),
                taskDto.isCompleted()
        );
    }

    public TaskDto mapToTaskDto (final Task task) {
        return new TaskDto (
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStartDate(),
                task.getUpdatedDate(),
                task.isCompleted(),
                task.getEndDate());
    }

    public List<TaskDto> mapToTaskDtoList (final List<Task> taskList) {
        return taskList.stream()
                .map(t-> new TaskDto(t.getId(), t.getTitle(), t.getDescription(), t.getStartDate(), t.getUpdatedDate(), t.isCompleted(), t.getEndDate()))
                .collect(Collectors.toList());
    }
}
