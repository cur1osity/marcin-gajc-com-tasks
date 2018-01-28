package com.marcingajc.tasks.trelloservice.validator;

import com.marcingajc.tasks.trelloservice.domain.TrelloBoard;
import com.marcingajc.tasks.trelloservice.domain.TrelloCard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class TrelloValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrelloValidator.class);

    public void validateCard(final TrelloCard trelloCard){

        if(trelloCard.getName().contains("test")){
            LOGGER.info("Someone is testing my application!");
        } else {
            LOGGER.info("Seems that my application is used in proper way.");
        }
    }

    public List<TrelloBoard> validateTrelloBoards(final List<TrelloBoard> trelloBoards) {
        LOGGER.info("Starting filtering boards...");
        List<TrelloBoard> filteredBoards = trelloBoards.stream()
                .filter(trelloBoard -> !trelloBoard.getName().equalsIgnoreCase("test"))
                .collect(toList());
        LOGGER.info("Boards has been filtered. Current list size: " + filteredBoards.size());
        return filteredBoards;
    }

    public TrelloBoard validateTrelloBoard(final TrelloBoard trelloBoard) {
        LOGGER.info("Starting filtering board...");

        String filteredName = "test";
        TrelloBoard filteredBoard;

        if(filteredName.equalsIgnoreCase(trelloBoard.getName())) {
            filteredBoard = null;
        } else {
            filteredBoard = trelloBoard;
        }
        LOGGER.info("Board has been filtered");
        return filteredBoard;
    }
}
