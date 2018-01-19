package com.marcingajc.tasks.client.controller;

import com.marcingajc.tasks.client.domain.MessageDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpStatusCodeException;

import java.io.IOException;

@ControllerAdvice
public class ExceptionHandlerController {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private TaskClientController taskController;

    @Autowired
    private TrelloController trelloController;

    @ExceptionHandler(HttpStatusCodeException.class)
    public String handleClientError(HttpStatusCodeException ex, Model model) throws IOException {

        if(ex.getStatusCode() == HttpStatus.BAD_REQUEST) {
            MessageDto dto = mapper.readValue(ex.getResponseBodyAsByteArray(), MessageDto.class);
            model.addAttribute("bad_request_error", dto.getMessages());
            return taskController.findAll(model);
        }

       if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
            MessageDto dto = mapper.readValue(ex.getResponseBodyAsByteArray(), MessageDto.class);
            model.addAttribute("not_found_error", dto.getMessages());
            return taskController.findAll(model);
        }

        if (ex.getStatusCode() == HttpStatus.NOT_ACCEPTABLE) {
            MessageDto dto = mapper.readValue(ex.getResponseBodyAsByteArray(), MessageDto.class);
            model.addAttribute("bad_request_error", dto.getMessages());
            return trelloController.findAll(model);
        }

        return "error";
    }
}
