package com.marcingajc.tasks.trelloservice.facade;

import com.marcingajc.tasks.trelloservice.domain.*;
import com.marcingajc.tasks.trelloservice.mapper.TrelloMapper;
import com.marcingajc.tasks.trelloservice.service.TrelloService;
import com.marcingajc.tasks.trelloservice.domain.CreatedTrelloCardDto;
import com.marcingajc.tasks.trelloservice.validator.TrelloValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TrelloFacade {

    @Autowired
    private TrelloService trelloService;

    @Autowired
    private TrelloMapper trelloMapper;

    @Autowired
    private TrelloValidator trelloValidator;

    public List<TrelloBoardDto> fetchTrelloBoards() {
        List<TrelloBoard> trelloBoards = trelloMapper.mapToBoards(trelloService.fetchTrelloBoards());
        List<TrelloBoard> filteredBoards = trelloValidator.validateTrelloBoards(trelloBoards);
        return trelloMapper.mapToBoardsDto(filteredBoards);
    }

    public CreatedTrelloCardDto createCard(final TrelloCardDto trelloCardDto){
        TrelloCard trelloCard = trelloMapper.mapToCard(trelloCardDto);
        trelloValidator.validateCard(trelloCard);
        return trelloService.createdTrelloCardDto(trelloMapper.mapToCardDto(trelloCard));
    }

    public TrelloBoardDto fetchTrelloBoard(String id) {
        TrelloBoard trelloBoard = trelloMapper.mapToBoard(trelloService.fetchTrelloBoard(id));
        trelloValidator.validateTrelloBoard(trelloBoard);
        return trelloMapper.mapToBoardDto(trelloBoard);
    }
}