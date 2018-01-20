package com.marcingajc.tasks.trelloservice.service;

import com.marcingajc.tasks.trelloservice.client.CreatedTrelloCard;
import com.marcingajc.tasks.trelloservice.client.TrelloClient;
//import com.marcingajc.tasks.trelloservice.config.AdminConfig;
//import com.marcingajc.tasks.trelloservice.domain.Mail;
import com.marcingajc.tasks.trelloservice.domain.TrelloBoardDto;
import com.marcingajc.tasks.trelloservice.domain.TrelloCardDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
//import static java.util.Optional.ofNullable;

@Service
public class TrelloService {

//    @Autowired
//    private AdminConfig adminConfig;

    @Autowired
    private TrelloClient trelloClient;

//    @Autowired
//    private SimpleEmailService emailService;

    private static final String SUBJECT = "Task: New Trello card";

    public List<TrelloBoardDto> fetchTrelloBoards() {
        return trelloClient.getTrelloBoards();
    }

    public TrelloBoardDto fetchTrelloBoard(String id) {
        return trelloClient.getTrelloBoard(id);
    }

    public CreatedTrelloCard createdTrelloCardDto(final TrelloCardDto trelloCardDto){
        CreatedTrelloCard newCard = trelloClient.createNewCard(trelloCardDto);

//        ofNullable(newCard).ifPresent(
//                card -> emailService.send(
//                        new Mail(
//                                adminConfig.getAdminMail(),
//                                SUBJECT,
//                                "New card: "  + card.getName() + " has been created on your Trello account")));

        return newCard;
    }
}
