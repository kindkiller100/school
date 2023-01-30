package com.school.school.groups;

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
@RequestMapping("/groups")
public class GroupController {
    @Autowired
    GroupService groupService;

    @GetMapping
    public Page<Group> getAll(Pageable pageable) {
        return groupService.getAll(pageable);
    }

    @PostMapping
    public void create(@Valid @RequestBody Group group) {
        groupService.create(group);
    }

    @PutMapping
    public void edit(@Valid @RequestBody Group group) {
        groupService.edit(group);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        groupService.delete(id);
    }
}
