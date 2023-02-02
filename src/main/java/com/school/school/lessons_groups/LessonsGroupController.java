package com.school.school.lessons_groups;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/lessons_groups")
public class LessonsGroupsController {
    @Autowired
    private LessonsGroupsService service;

    @GetMapping
    public Page<LessonsGroups> list(Pageable pageable) {
        return service.list(pageable);
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
