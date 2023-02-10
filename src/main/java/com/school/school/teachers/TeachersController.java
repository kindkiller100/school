package com.school.school.teachers;

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
@RequestMapping("/teachers")
public class TeachersController {
    @Autowired
    private TeacherService teacherService;

    @GetMapping("/{id}")
    public Teacher getById(@PathVariable long id) {
        return teacherService.getIfExists(id);
    }

    @GetMapping
    public Page<Teacher> getAll(Pageable pageable) {
        return teacherService.getAll(pageable);
    }

    @GetMapping("/deleted")
    public Page<Teacher> getAllDeleted(Pageable pageable) {
        return teacherService.getAllDeleted(pageable);
    }

    @GetMapping("/filter/{like}")
    public Page<Teacher> getAllByFilter(@PathVariable String like, Pageable pageable) {
        return teacherService.getAllByFilter(like, pageable);
    }

    @PostMapping
    public void create(@Valid @RequestBody Teacher teacher) {
        teacherService.create(teacher);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        teacherService.delete(id);
    }
    @DeleteMapping("/wipe")
    public void wipe() {
        teacherService.wipe();
    }

    @PutMapping("/edit")
    public void edit(@Valid @RequestBody Teacher editTeacher) {
        teacherService.edit(editTeacher);
    }

    @PutMapping("/{id}/restore")
    public void restoreDeleted(@PathVariable long id) {
        teacherService.restoreDeleted(id);
    }

}
