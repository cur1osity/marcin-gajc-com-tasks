package com.marcingajc.tasks.client.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class MessageDto {

    private List<String> messages;
}
