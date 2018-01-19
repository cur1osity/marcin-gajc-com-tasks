package com.marcingajc.tasks.client.service;

import com.marcingajc.tasks.client.controller.TaskServiceUnavailableEx;
import com.marcingajc.tasks.client.domain.TaskDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskClientService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskClientService.class);

    @Value("${resource.tasks}")
    private String resource;

    @Value("${resource.tasks}/{id}")
    private String idResource;

    @Autowired
    private RestTemplate restTemplate;


    public List<TaskDto> findAll() throws TaskServiceUnavailableEx {

        try {
            restTemplate.getForObject(resource, TaskDto[].class);
        } catch (ResourceAccessException ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw new TaskServiceUnavailableEx();
        }
        return Arrays.stream(restTemplate.getForObject(resource, TaskDto[].class)).collect(Collectors.toList());
    }

    public TaskDto update(Long id, TaskDto task) {
        return restTemplate.exchange(idResource, HttpMethod.PATCH, new HttpEntity<>(task), TaskDto.class, id).getBody();
    }

    public void delete(Long id) {
        restTemplate.delete(idResource, id);
    }

    public TaskDto create(TaskDto task) {
        return restTemplate.postForObject(resource, task, TaskDto.class);
    }

    public void deleteAllTask() {
        restTemplate.delete(resource);
    }

    public TaskDto findTask(Long id) {
        return restTemplate.getForObject(idResource, TaskDto.class, id);
    }
}
