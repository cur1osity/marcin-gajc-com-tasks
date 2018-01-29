package com.marcingajc.tasks.api.domain;

import lombok.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "taskDtoBuilder")
public class TaskDto {

    private Long id;

    @NotBlank(message = "NotBlank.taskDto.title")
    @Size(min = 3, max = 64, message = "Size.taskDto.title")
    private String title;

    @NotBlank(message = "NotBlank.taskDto.description")
    @Size(min = 3, max = 255, message = "Size.taskDto.description")
    private String description;
    private String startDate;
    private String updatedDate;
    private boolean completed;
    private String endDate;
}
