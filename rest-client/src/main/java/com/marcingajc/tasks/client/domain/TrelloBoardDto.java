package com.marcingajc.tasks.client.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

@AllArgsConstructor
@Getter
@JsonIgnoreProperties( ignoreUnknown = true )
public class TrelloBoardDto {

    private int idX;

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty
    private List<TrelloListDto> lists;

}

