package com.marcingajc.tasks.api.controller;

import com.marcingajc.tasks.api.customvalidator.PathValidator;
import com.marcingajc.tasks.api.domain.MessageDto;
import com.marcingajc.tasks.api.domain.Task;
import com.marcingajc.tasks.api.domain.TaskDto;
import com.marcingajc.tasks.api.mapper.TaskMapper;
import com.marcingajc.tasks.api.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Validated
@RequestMapping("/v1/tasks")
@CrossOrigin(origins = "*")
public class TaskController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    private TaskService service;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private MessageSource messageSource;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TaskDto> getTasks() {
        List<Task> allTasks = service.getAllTasks();
        return taskMapper.mapToTaskDtoList(allTasks);
    }

    @GetMapping({"/{id}"})
    @ResponseStatus(HttpStatus.OK)
    public TaskDto getTask(@PathValidator @PathVariable Long id) {
        final Task task = service.getTask(id);
        return taskMapper.mapToTaskDto(task);
    }

    @DeleteMapping({"/{id}"})
    @ResponseStatus(HttpStatus.OK)
    public void deleteTask(@PathValidator @PathVariable Long id) {

        service.deleteTask(id);
    }

    @PatchMapping(value = {"/{id}"}, consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public TaskDto updateTask(@PathValidator @PathVariable Long id, @Valid @RequestBody TaskDto taskDto) {
        Task task = taskMapper.mapToTask(taskDto);
        Task taskAfterUpdate = service.saveTaskWithId(id, task);
        return taskMapper.mapToTaskDto(taskAfterUpdate);
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void createTask(@Valid @RequestBody TaskDto taskDto) {
        final Task taskFromJSON = taskMapper.mapToTask(taskDto);
        service.saveTask(taskFromJSON);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteTasks() {
        service.deleteAllTask();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public MessageDto handleValidationException(MethodArgumentNotValidException ex, Locale locale) {
        BindingResult result = ex.getBindingResult();
        List<String> errorMessages = result.getAllErrors()
                .stream()
                .map(objectError -> messageSource.getMessage(objectError, locale))
                .collect(Collectors.toList());
        LOGGER.error(ex.getMessage(), ex);
        return new MessageDto(errorMessages);
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(ConstraintViolationException.class)
    public MessageDto handle(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        StringBuilder strBuilder = new StringBuilder();
        for (ConstraintViolation<?> violation : violations ) {
            strBuilder.append(violation.getMessage());
        }
        List<String> errorList = new ArrayList<>();
        errorList.add(strBuilder.toString());
        LOGGER.error(e.getMessage(), e);
        return new MessageDto(errorList);
    }
}
