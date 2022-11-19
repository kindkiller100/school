package com.school.school.students;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/students")
public class StudentsController {

    @GetMapping
    public List<Student> getAll(){
        return StudentsStorage.getAll();
    }

    @PostMapping
    public void create (@RequestBody Student student) {
        StudentsStorage.create(student);
    }
    @GetMapping("/{id}")
    public Student getById (@PathVariable UUID id) {
        return StudentsStorage.getById(id);
    }
    @PutMapping("/{id}")
    public void updateById (@PathVariable UUID id, @RequestBody Student newStudent) {
        StudentsStorage.update(id, newStudent);
    }

    @DeleteMapping("/{id}")
    public void deleteById (@PathVariable UUID id) {
        StudentsStorage.delete(id);
    }
}
