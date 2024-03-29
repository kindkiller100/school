package com.school.school.subjects;

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
@RequestMapping("/subjects")
public class SubjectController {

    @Autowired
    SubjectService service;

    @GetMapping
    public Page<Subject> getAll(Pageable pageable) {
        return service.getAll(pageable);
    }

    @GetMapping("/deleted")
    public Page<Subject> getAllDeleted(Pageable pageable) {
        return service.getAllDeleted(pageable);
    }

    @PostMapping
    public void create(@Valid @RequestBody Subject subject) {
        service.create(subject);
    }

    @PutMapping
    public void edit(@Valid @RequestBody Subject subject) {
        service.edit(subject);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        service.delete(id);
    }

    @DeleteMapping("/wipe")
    public void wipe() {
        service.wipe();
    }

    @PutMapping("/{id}/restore")
    public void restoreDeleted(@PathVariable long id) {
        service.restoreDeleted(id);
    }

}
