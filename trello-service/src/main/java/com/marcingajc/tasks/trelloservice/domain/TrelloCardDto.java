package com.marcingajc.tasks.trelloservice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Getter
public class TrelloCardDto {

    @NotBlank(message = "Title can't be blank")
    private String name;
    @NotBlank(message = "Description can't be blank")
    private String description;
    private String pos;
    @NotBlank(message = "Please select Trello List")
    private String listId;
}