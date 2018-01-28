package com.marcingajc.tasks.trelloservice.client;

import com.marcingajc.tasks.trelloservice.config.TrelloConfig;
import com.marcingajc.tasks.trelloservice.domain.CreatedTrelloCardDto;
import com.marcingajc.tasks.trelloservice.domain.TrelloBoardDto;
import com.marcingajc.tasks.trelloservice.domain.TrelloCardDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Optional.ofNullable;

@Component
public class TrelloClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrelloClient.class);

    @Autowired
    private TrelloConfig trelloConfig;

    @Autowired
    private RestTemplate restTemplate;


    private URI getTrelloBoardsURL() {

        return UriComponentsBuilder.fromHttpUrl
                (trelloConfig.getTrelloApiEndpoint() + "/members/" + trelloConfig.getTrelloUsername() + "/boards")
                .queryParam("key", trelloConfig.getTrelloAppKey())
                .queryParam("token", trelloConfig.getTrelloToken())
                .queryParam("fields", "name,id")
                .queryParam("lists", "open")
                .build().encode().toUri();
    }

    public List<TrelloBoardDto> getTrelloBoards() {

        try {
            TrelloBoardDto[] boardsResponse = restTemplate.getForObject(getTrelloBoardsURL(), TrelloBoardDto[].class);
            return Arrays.asList(ofNullable(boardsResponse).orElse(new TrelloBoardDto[0]));

        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    private URI getTrelloBoardURL(String id) {

        return UriComponentsBuilder.fromHttpUrl
                (trelloConfig.getTrelloApiEndpoint() + "/board/" + id)
                .queryParam("key", trelloConfig.getTrelloAppKey())
                .queryParam("token", trelloConfig.getTrelloToken())
                .queryParam("fields", "name,id")
                .queryParam("lists", "open")
                .build().encode().toUri();
    }

    public TrelloBoardDto getTrelloBoard(String id) {

        try {
            TrelloBoardDto boardsResponse = restTemplate.getForObject(getTrelloBoardURL(id), TrelloBoardDto.class);
            return ofNullable(boardsResponse).orElse(new TrelloBoardDto());

        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return new TrelloBoardDto();
        }
    }

    public CreatedTrelloCardDto createNewCard(TrelloCardDto trelloCardDto) {

        URI url = UriComponentsBuilder.fromHttpUrl(
                trelloConfig.getTrelloApiEndpoint() + "/cards")
                .queryParam("key", trelloConfig.getTrelloAppKey())
                .queryParam("token", trelloConfig.getTrelloToken())
                .queryParam("name", trelloCardDto.getName())
                .queryParam("desc", trelloCardDto.getDescription())
                .queryParam("pos", trelloCardDto.getPos())
                .queryParam("idList", trelloCardDto.getListId())
                .build().encode().toUri();

        return restTemplate.postForObject(url, null, CreatedTrelloCardDto.class);
    }
}