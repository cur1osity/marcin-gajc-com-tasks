package com.marcingajc.tasks.api.mapper;

import com.marcingajc.tasks.api.domain.Task;
import com.marcingajc.tasks.api.domain.TaskDto;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class TaskMapperTest {

    private static final Long TASK_DTO1_ID = 1L;
    private static final Long TASK1_ID = 1L;
    private static TaskDto TASK_DTO1;
    private static Task TASK1;
    private static Task TASK2;

    @Before
    public void taskInit() {

        TASK_DTO1 = TaskDto.taskDtoBuilder()
                .id(TASK_DTO1_ID)
                .title("Test1")
                .description("Test1")
                .startDate("start_date")
                .updatedDate("updated_date")
                .completed(true)
                .build();

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
    }

    @InjectMocks
    private TaskMapper mapper;

    @Test
    public void should_mapToTask(){

        // Given & When
        Task task = mapper.mapToTask(TASK_DTO1);

        // Then
        Assert.assertEquals(1L, task.getId().longValue());
        Assert.assertEquals("Test1", task.getTitle());
        Assert.assertEquals("Test1", task.getDescription());
    }

    @Test
    public void should_mapToTaskDto(){

        // Given & When
        TaskDto taskDto = mapper.mapToTaskDto(TASK1);

        // Then
        Assert.assertEquals(1L, taskDto.getId().longValue());
        Assert.assertEquals("Test1", taskDto.getTitle());
        Assert.assertEquals("Test1", taskDto.getDescription());
    }

    @Test
    public void should_mapToTaskDtoList(){

        // Given
        List<Task> taskList = new ArrayList<>();
        taskList.add(TASK1);
        taskList.add(TASK2);

        // When
        List<TaskDto> taskDtos =  mapper.mapToTaskDtoList(taskList);

        // Then
        assertNotNull(taskDtos);
        assertEquals(2, taskDtos.size());
        assertEquals("Test1", taskDtos.get(1).getTitle());
        assertEquals(1, taskDtos.get(0).getId().longValue());
    }
}