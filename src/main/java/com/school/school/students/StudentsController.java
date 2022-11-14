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
        return StudentsStorage.data;
    }

    @PostMapping
    public Student create (@RequestBody Student student) {
        if (student.getId() == null) {
            student.generateId();
        }
        StudentsStorage.data.add(student);
        return student;
    }
}
