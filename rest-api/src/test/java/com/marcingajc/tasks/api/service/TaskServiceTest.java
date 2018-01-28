package com.marcingajc.tasks.api.service;

import com.marcingajc.tasks.api.domain.Task;
import com.marcingajc.tasks.api.repository.TaskRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.Arrays;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


@RunWith(MockitoJUnitRunner.class)
public class TaskServiceTest {

    private static final Long TASK1_ID = 1L;
    private static Task TASK1;
    private static Task TASK2;
    private static Task NEW_TASK;

    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;

    private ArgumentCaptor<Task> anyTask = ArgumentCaptor.forClass(Task.class);

    @Before
    public void tasksInit() {

        TASK1 =  Task.taskBuilder()
                .id(TASK1_ID)
                .title("Test1")
                .description("Test1")
                .startDate("start_date")
                .updatedDate("updated_date")
                .completed(true)
                .build();

        TASK2 = Task.taskBuilder()
                .id(2L)
                .title("Test1")
                .description("Test1")
                .startDate("start_date")
                .updatedDate("updated_date")
                .completed(true)
                .build();

        NEW_TASK = Task.taskBuilder()
                .title("Test1")
                .description("Test1")
                .startDate("start_date")
                .updatedDate("updated_date")
                .completed(true)
                .build();
    }

    @Test
    public void whenFindingTasksItShouldReturnAllTasks() {

        // Given
        given(taskRepository.findAll()).willReturn(Arrays.asList(TASK1, TASK2));
        // When
        List<Task> tasks = taskService.getAllTasks();
        // Then
        assertThat(tasks)
                .containsOnly(TASK1, TASK2);
    }

    @Test
    public void whenFindingTaskWithGivenIdItShouldReturnTaskWithGivenId() {
        // Given
        given(taskRepository.getOne(TASK1_ID)).willReturn(TASK1);
        // When
        Task task = taskService.getTask(TASK1_ID);
        // Then
        assertThat(task).isEqualTo(TASK1);
    }

    @Test
    public void whenAddingTaskItShouldReturnTheSavedTask() {
        // Given
        given(taskRepository.save(NEW_TASK)).willReturn(TASK1);
        // When
        assertThat(taskService.saveTask(NEW_TASK))
                // Then
                .isSameAs(TASK1);
    }

    @Test
    public void whenAddingTaskItShouldMakeSureNoIDIsPassed() {
        // Given
        taskService.saveTask(TASK1);
        // When
        verify(taskRepository).save(anyTask.capture());
        // Then
        assertThat(anyTask.getValue().getId()).isNull();
    }

    @Test
    public void whenUpdatingTaskItShouldUseTheGivenID() {
        // Given & When
        taskService.saveTaskWithId(TASK1_ID, NEW_TASK);
        // Then
        verify(taskRepository).save(anyTask.capture());
        assertThat(anyTask.getValue().getId()).isEqualTo(TASK1_ID);
    }

    @Test
    public void whenDeletingAnTaskItShouldUseTheRepository() {
        // Given & When
        taskService.deleteTask(TASK1_ID);
        // Then
        verify(taskRepository).deleteById(TASK1_ID);
    }

    @Test
    public void whenDeletingTasksItShouldUseTheRepository() {
        // Given & When
        taskService.deleteAllTask();
        // Then
        verify(taskRepository).deleteAllInBatch();
    }
}