package com.school.school.students_groups;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/students_groups")
public class StudentsGroupController {
    @Autowired
    StudentsGroupService studentsGroupService;

    @GetMapping
    public Page<StudentsGroup> getAll(Pageable pageable) {
        return studentsGroupService.getAll(pageable);
    }

    @PostMapping
    public void create(@Valid @RequestBody StudentsGroup studentsGroup) {
        studentsGroupService.create(studentsGroup);
    }

    @PutMapping
    public void edit(@Valid @RequestBody StudentsGroup studentsGroup) {
        studentsGroupService.edit(studentsGroup);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        studentsGroupService.delete(id);
    }
}
