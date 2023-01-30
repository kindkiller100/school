package com.school.school.groups;

import com.school.school.students.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/groups")
public class GroupController {
    @Autowired
    GroupService service;

    @GetMapping("/{id}/students")
    public Page<Student> getAllStudentsInGroup(@PathVariable long id, Pageable pageable){
        return service.getAllStudentsInGroup(id, pageable);
    }
    @GetMapping
    public Page<Group> getAll(Pageable pageable) {
        return service.getAll(pageable);
    }

    @PostMapping
    public void create(@Valid @RequestBody Group group) {
        service.create(group);
    }

    @PutMapping
    public void edit(@Valid @RequestBody Group group) {
        service.edit(group);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        service.delete(id);
    }
}
