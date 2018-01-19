package com.marcingajc.tasks.client.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class TaskServiceUnavailableEx extends Exception {
}
