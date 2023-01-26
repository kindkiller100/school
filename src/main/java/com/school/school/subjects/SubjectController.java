package com.school.school.subjects;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/subjects")
public class SubjectController {

    @Autowired
    SubjectService subjectService;

    @GetMapping
    public List<Subject> getAll() {
        return subjectService.getAll();
    }

    @GetMapping("/deleted")
    public List<Subject> getAllDeleted() {
        return subjectService.getAllDeleted();
    }

    @PostMapping
    public void create(@Valid @RequestBody Subject subject) {
        subjectService.create(subject);
    }

    @PutMapping
    public void edit(@Valid @RequestBody Subject subject) {
        subjectService.edit(subject);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        subjectService.delete(id);
    }

    @PutMapping("/{id}/restore")
    public void restoreDeleted(@PathVariable long id) {
        subjectService.restoreDeleted(id);
    }

}
