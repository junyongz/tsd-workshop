package com.tsd.workshop.migration.tasks.web;

import com.tsd.workshop.migration.tasks.data.MigTask;
import com.tsd.workshop.migration.tasks.data.MigTaskSqlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/mig-tasks")
public class MigTaskController {

    @Autowired
    private MigTaskSqlRepository migTaskSqlRepository;

    @GetMapping
    public Flux<MigTask> search(@RequestParam(value = "keyword", required = false) String keyword,
                                         @RequestParam(value = "workshopTasks", required = false) String workshopTasks,
                                @RequestParam(value = "subsystem", required = false) String subsystem) {
        if (workshopTasks != null) {
            return migTaskSqlRepository.searchByTasks(workshopTasks, subsystem);
        }
        if (keyword != null) {
            return migTaskSqlRepository.searchByKeyword(keyword);
        }

        return migTaskSqlRepository.findAll();
    }
}
