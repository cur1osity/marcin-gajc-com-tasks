package com.marcingajc.tasks.trelloservice.controller;

import com.marcingajc.tasks.trelloservice.domain.CreatedTrelloCardDto;
import com.marcingajc.tasks.trelloservice.domain.MessageDto;
import com.marcingajc.tasks.trelloservice.domain.TrelloBoardDto;
import com.marcingajc.tasks.trelloservice.domain.TrelloCardDto;
import com.marcingajc.tasks.trelloservice.facade.TrelloFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/v1/trello")
public class TrelloController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrelloController.class);

    @Autowired
    private TrelloFacade trelloFacade;

    @Autowired
    private MessageSource messageSource;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<TrelloBoardDto> getTrelloBoards() {
        return trelloFacade.fetchTrelloBoards();
    }

    @GetMapping("/{id}")
    public TrelloBoardDto getTrelloBoard(@PathVariable String id) {
        return trelloFacade.fetchTrelloBoard(id);
    }

    @PostMapping
    public CreatedTrelloCardDto createdTrelloCard(@Valid @RequestBody TrelloCardDto trelloCardDto) {

        return trelloFacade.createCard(trelloCardDto);

    }

    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public MessageDto handleValidationException(MethodArgumentNotValidException ex, Locale locale) {
        BindingResult result = ex.getBindingResult();
        List<String> errorMessages = result.getAllErrors()
                .stream()
                .map(objectError -> messageSource.getMessage(objectError, locale))
                .collect(Collectors.toList());
        LOGGER.error(ex.getMessage(), ex);
        return new MessageDto(errorMessages);
    }
}
