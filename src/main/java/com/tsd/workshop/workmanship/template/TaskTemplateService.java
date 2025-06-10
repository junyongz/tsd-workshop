package com.tsd.workshop.workmanship.template;

import com.tsd.workshop.workmanship.template.data.Task;
import com.tsd.workshop.workmanship.template.data.TaskComponentRepository;
import com.tsd.workshop.workmanship.template.data.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class TaskTemplateService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskComponentRepository taskComponentRepository;

    public Flux<Task> getAllTasks() {
        return taskRepository.findAll(Sort.by(Sort.Order.asc("labourHours")))
                .flatMapSequential(task ->
                    taskComponentRepository.findById(task.getComponentId())
                            .map(taskComponent -> {
                                task.setComponent(taskComponent);
                                return task;
                            })
                );
    }
}
