package com.school.school.students_groups;

import com.school.school.groups.GroupService;
import com.school.school.students.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/students_groups")
public class StudentsGroupController {
    @Autowired
    StudentsGroupService service;

    @GetMapping("/{id}/students")
    public Page<Student> getAllStudentsInGroup(@PathVariable long id, Pageable pageable){
        return service.getAllStudentsInGroup(id, pageable);
    }
    @GetMapping
    public Page<StudentsGroup> getAll(Pageable pageable) {
        return service.getAll(pageable);
    }

    @PostMapping
    public void create(@Valid @RequestBody StudentsGroup group) {
        service.create(group);
    }

    @PutMapping
    public void edit(@Valid @RequestBody StudentsGroup group) {
        service.edit(group);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        service.delete(id);
    }


}
