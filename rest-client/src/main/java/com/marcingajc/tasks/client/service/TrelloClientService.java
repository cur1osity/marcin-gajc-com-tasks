package com.marcingajc.tasks.client.service;

import com.marcingajc.tasks.client.controller.TrelloServiceUnavailableEx;
import com.marcingajc.tasks.client.domain.CreatedTrelloCardDto;
import com.marcingajc.tasks.client.domain.TrelloBoardDto;
import com.marcingajc.tasks.client.domain.TrelloCardDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TrelloClientService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrelloClientService.class);

    @Value("${resource.trelloBoards}")
    private String resource;

    @Value("${resource.trelloBoards}/{id}")
    private String idResource;

    @Autowired
    private RestTemplate restTemplate;


    public List<TrelloBoardDto> findAll() throws TrelloServiceUnavailableEx  {

        try {
            restTemplate.getForObject(resource, TrelloBoardDto[].class);
        } catch (ResourceAccessException ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw new TrelloServiceUnavailableEx();

        }
        return Arrays.stream(restTemplate.getForObject(resource, TrelloBoardDto[].class)).collect(Collectors.toList());
    }

    public TrelloBoardDto findOne(String id)  {

        return restTemplate.getForObject(idResource, TrelloBoardDto.class, id);
    }

    public CreatedTrelloCardDto createTrelloCard(TrelloCardDto trelloCardDto) {
        return restTemplate.postForObject(resource, trelloCardDto, CreatedTrelloCardDto.class);
    }
}
