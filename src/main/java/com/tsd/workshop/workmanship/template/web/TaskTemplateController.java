package com.tsd.workshop.workmanship.template.web;

import com.tsd.workshop.workmanship.template.TaskTemplateService;
import com.tsd.workshop.workmanship.template.data.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/tasks")
public class TaskTemplateController {

    @Autowired
    private TaskTemplateService taskTemplateService;

    @GetMapping
    public Flux<Task> getAllTasks() {
        return taskTemplateService.getAllTasks();
    }
}
