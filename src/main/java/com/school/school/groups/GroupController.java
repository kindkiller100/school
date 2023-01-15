package com.school.school.groups;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/groups")
public class GroupController {
    @Autowired
    GroupService groupService;

    @GetMapping
    public List<Group> getAll() {
        return groupService.getAll();
    }

    @PostMapping
    public void create(@Valid @RequestBody Group group) {
        groupService.create(group);
    }

    @PutMapping
    public void edit(@Valid @RequestBody Group group) {
        groupService.edit(group);
    }

    //TODO: add method delete()
}
