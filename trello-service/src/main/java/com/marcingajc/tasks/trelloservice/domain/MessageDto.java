package com.marcingajc.tasks.trelloservice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MessageDto {

    private List<String> messages;
}
