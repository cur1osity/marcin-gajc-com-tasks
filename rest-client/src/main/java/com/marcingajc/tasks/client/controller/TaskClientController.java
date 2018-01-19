package com.marcingajc.tasks.client.controller;

import com.marcingajc.tasks.client.domain.TaskDto;
import com.marcingajc.tasks.client.service.TaskClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;


@Controller
@RequestMapping("/tasks")
public class TaskClientController {

    @Autowired
    private TaskClientService service;

    @GetMapping
    public String findAll(Model model) {
        try {
            model.addAttribute("tasks", service.findAll());
            model.addAttribute("newTask", new TaskDto());
        } catch (TaskServiceUnavailableEx ex) {
            noService(model);
        }
        return "tasks";
    }

    @GetMapping({"/{id}"})
    @ResponseStatus(HttpStatus.OK)
    public String findOne(Model model, @PathVariable Long id) {
        try {
            model.addAttribute("tasks", service.findAll());
            model.addAttribute("task", service.findTask(id));
            model.addAttribute("newTask", new TaskDto());
        } catch (TaskServiceUnavailableEx ex) {
            noService(model);
        }
        return "tasks";
    }

    @GetMapping({"/noService"})
    public String noService(Model model) {
        model.addAttribute("tasks", new ArrayList<>());
        model.addAttribute("newTask", new TaskDto());
        model.addAttribute("noService", "serviceUnvailable");
        return "tasks";
    }

    @PatchMapping({"/{id}"})
    public String update(@PathVariable Long id, TaskDto task, RedirectAttributes model) {
        model.addFlashAttribute("message_update", "success");
        service.update(id, task);
        return "redirect:/tasks";
    }

    @DeleteMapping({"/{id}"})
    public String delete(@PathVariable Long id, RedirectAttributes model) {
        model.addFlashAttribute("message_delete", "success");
        service.delete(id);
        return "redirect:/tasks";
    }

    @DeleteMapping
    public String deleteAll(RedirectAttributes model) {
        model.addFlashAttribute("message_deleteAll", "success");
        service.deleteAllTask();
        return "redirect:/tasks";
    }

    @PostMapping
    public String create(@ModelAttribute("newTask") TaskDto task, RedirectAttributes model) {
        model.addFlashAttribute("message_post", "success");
        service.create(task);
        return "redirect:/tasks";
    }
}
