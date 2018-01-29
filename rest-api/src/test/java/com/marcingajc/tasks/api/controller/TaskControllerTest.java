package com.marcingajc.tasks.api.controller;

import com.google.gson.Gson;
import com.marcingajc.tasks.api.domain.Task;
import com.marcingajc.tasks.api.domain.TaskDto;
import com.marcingajc.tasks.api.mapper.TaskMapper;
import com.marcingajc.tasks.api.repository.TaskRepository;
import com.marcingajc.tasks.api.service.TaskService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    private static final Long TASK_DTO1_ID = 1L;
    private static TaskDto TASK_DTO1;
    private static TaskDto TASK_DTO2;
    private static final Long TASK1_ID = 1L;
    private static Task TASK1;
    private static Task TASK2;
    private static Task NEW_TASK;
    private static Task BAD_TASK;
    private static TaskDto BAD_TASK_DTO;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @MockBean
    private TaskMapper taskMapper;

    @MockBean
    private TaskRepository taskRepository;

    @Before
    public void tasksInit() {

        TASK_DTO1 = TaskDto.taskDtoBuilder()
                .id(TASK_DTO1_ID)
                .title("Test1")
                .description("Test1")
                .startDate("start_date")
                .updatedDate("updated_date")
                .completed(true)
                .build();

        TASK_DTO2 = TaskDto.taskDtoBuilder()
                .id(2L)
                .title("Test2")
                .description("Test2")
                .startDate("start_date")
                .updatedDate("updated_date")
                .completed(false)
                .build();

        BAD_TASK_DTO = TaskDto.taskDtoBuilder()
                .title("T")
                .description("T")
                .startDate("start_date")
                .updatedDate("updated_date")
                .completed(false)
                .build();

        TASK1 = Task.taskBuilder()
                .id(TASK1_ID)
                .title("Test1")
                .description("Test1")
                .startDate("start_date")
                .updatedDate("updated_date")
                .completed(true)
                .build();

        TASK2 = Task.taskBuilder()
                .id(2L)
                .title("Test2")
                .description("Test2")
                .startDate("start_date")
                .updatedDate("updated_date")
                .completed(false)
                .build();

        BAD_TASK = Task.taskBuilder()
                .id(1L)
                .title("T")
                .description("T")
                .startDate("start_date")
                .updatedDate("updated_date")
                .completed(false)
                .build();

        NEW_TASK = Task.taskBuilder()
                .id(1L)
                .title("Test1")
                .description("Test1")
                .startDate("start_date")
                .updatedDate(null)
                .completed(false)
                .build();
    }

    @Test
    public void shouldGetEmptyTasksList() throws Exception {
        // Given
        List<Task> tasks = new ArrayList<>();
        List<TaskDto> taskDtos = new ArrayList<>();

        given(taskService.getAllTasks()).willReturn(tasks);
        given(taskMapper.mapToTaskDtoList(tasks)).willReturn(taskDtos);

        // When & Then
        mockMvc.perform(get("/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void shouldGetAllTasksFromTasksLists() throws Exception {

        // Given
        given(taskService.getAllTasks()).willReturn(Arrays.asList(TASK1, TASK2));
        given(taskMapper.mapToTaskDtoList(Arrays.asList(TASK1, TASK2))).willReturn(Arrays.asList(TASK_DTO1, TASK_DTO2));

        // When & Then
        mockMvc.perform(get("/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("Test1")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].title", is("Test2")));
    }

    @Test
    public void shouldGetTaskWithPathIdFromTaskList() throws Exception {

        //Given
        given(taskService.getTask(TASK1_ID)).willReturn(TASK1);
        given(taskMapper.mapToTaskDto(TASK1)).willReturn(TASK_DTO1);
        given(taskRepository.existsById(TASK1_ID)).willReturn(true);

        //When & Then
        mockMvc.perform(get("/v1/tasks/{id}", TASK1_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Test1")));
    }

    @Test
    public void shouldNotGetTaskWithBadPathIdFromTaskList() throws Exception {

        //Given
        given(taskService.getTask(TASK1_ID)).willReturn(TASK1);
        given(taskMapper.mapToTaskDto(TASK1)).willReturn(TASK_DTO1);
        given(taskRepository.existsById(TASK1_ID)).willReturn(false);

        //When & Then
        mockMvc.perform(get("/v1/tasks/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldDeleteTaskWithGivenId() throws Exception {
        //Given & When & Then

        given(taskRepository.existsById(TASK1_ID)).willReturn(true);

        mockMvc.perform(delete("/v1/tasks/{id}", TASK1_ID))
                .andExpect(status().isOk());
        verify(taskService, times(1)).deleteTask(TASK1_ID);
    }

    @Test
    public void shouldDeleteAllTasks() throws Exception {
        //Given & When & Then
        mockMvc.perform(delete("/v1/tasks"))
                .andExpect(status().isOk());
        verify(taskService, times(1)).deleteAllTask();
    }

    @Test
    public void shouldUpdateTaskWithGivenId() throws Exception {
        //Given
        given(taskMapper.mapToTask(TASK_DTO2)).willReturn(TASK2);
        given(taskService.saveTaskWithId(2L, TASK2)).willReturn(TASK2);
        given(taskRepository.existsById(2L)).willReturn(true);
        given(taskMapper.mapToTaskDto(TASK2)).willReturn(TASK_DTO2);

        Gson gson = new Gson();

        String jsonContent = gson.toJson(TASK_DTO2);
        // When & Then
        mockMvc.perform(patch("/v1/tasks/{id}", 2L)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.title", is("Test2")));
    }

    @Test
    public void shouldNotUpdateTaskWhenTaskDoesntExist() throws Exception {
        //Given
        given(taskMapper.mapToTask(TASK_DTO2)).willReturn(TASK2);
        given(taskService.saveTaskWithId(2L, TASK2)).willReturn(TASK2);
        given(taskRepository.existsById(2L)).willReturn(false);
        given(taskMapper.mapToTaskDto(TASK2)).willReturn(TASK_DTO2);

        Gson gson = new Gson();
        String jsonContent = gson.toJson(TASK_DTO2);
        //When & Then
        mockMvc.perform(patch("/v1/tasks/{id}", 2L)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonContent))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldNotUpdateTaskWhenValidationisFailed() throws Exception {
        //Given
        given(taskMapper.mapToTask(BAD_TASK_DTO)).willReturn(BAD_TASK);
        given(taskService.saveTaskWithId(1L, BAD_TASK)).willReturn(BAD_TASK);
        given(taskRepository.existsById(1L)).willReturn(true);
        given(taskMapper.mapToTaskDto(BAD_TASK)).willReturn(BAD_TASK_DTO);

        Gson gson = new Gson();
        String jsonContent = gson.toJson(BAD_TASK_DTO);
        //When & Then
        mockMvc.perform(patch("/v1/tasks/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonContent))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldPostTaskWithoutId() throws Exception {
        //Given
        Gson gson = new Gson();
        String jsonContent = gson.toJson(NEW_TASK);
        //When & Then
        mockMvc.perform(post("/v1/tasks/")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonContent))
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldPostTaskWithId() throws Exception {
        //Given
        Gson gson = new Gson();
        String jsonContent = gson.toJson(TASK1);
        //When & Then
        mockMvc.perform(post("/v1/tasks/")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonContent))
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldNotPostTaskWhenVailidationIsFailed() throws Exception {
        //Given
        Gson gson = new Gson();
        String jsonContent = gson.toJson(BAD_TASK);
        //When & Then
        mockMvc.perform(post("/v1/tasks/")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonContent))
                .andExpect(status().isBadRequest());
    }
}