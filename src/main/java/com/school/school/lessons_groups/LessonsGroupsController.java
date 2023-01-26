package com.school.school.lessons_groups;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/lessons_groups")
public class LessonsGroupsController {
    @Autowired
    private LessonsGroupsService service;

    @GetMapping
    public List<LessonsGroups> list() {
        return service.list();
    }

    @GetMapping("/{id}")
    public LessonsGroups getById(@PathVariable long id) {
        return service.getIfExists(id);
    }

    @PostMapping
    public void create(@Valid @RequestBody LessonsGroups lessonsGroups) {
        service.create(lessonsGroups);
    }

    @PutMapping
    public void edit(@Valid @RequestBody LessonsGroups lessonsGroups) {
        service.edit(lessonsGroups);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        service.delete(id);
    }
}
