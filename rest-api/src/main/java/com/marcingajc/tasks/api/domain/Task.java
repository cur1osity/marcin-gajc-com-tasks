package com.marcingajc.tasks.api.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@NoArgsConstructor
@Data
@Entity(name = "tasks")
@AllArgsConstructor
@Builder(builderMethodName = "taskBuilder")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "start_date")
    private String startDate;
    @Column(name = "last_modified")
    private String updatedDate;
    @Column(name = "completed")
    private boolean completed;
    @Column(name = "completion_date")
    private String endDate;
}
