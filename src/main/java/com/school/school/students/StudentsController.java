package com.school.school.students;

import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public Student getById (@PathVariable Long id) {
        return StudentsStorage.getById(id);
    }
    @PutMapping("/{id}")
    public void updateById (@PathVariable Long id, @RequestBody Student newStudent) {
        StudentsStorage.update(id, newStudent);
    }

    @DeleteMapping("/{id}")
    public void deleteById (@PathVariable Long id) {
        StudentsStorage.delete(id);
    }

//    @PostMapping
//    public Student create(@RequestBody Student student) {
//        if (student.getId() == null) {
//            student.generateId();
//        }
//
//        StudentsStorage.data.add(student);
//
//        return student;
//    }
//
//    @GetMapping("/{id}")
//    public Student getById(@PathVariable UUID id) {
//        return StudentsStorage.getById(id);
//    }
//
//    @PutMapping("/{id}")
//    public void editById(@PathVariable UUID id, @RequestBody Student newStudent) {
//        Student student = StudentsStorage.getById(id);
//
//        if (newStudent.getName() != null && !student.getName().equals(newStudent.getName())) {
//            student.setName(newStudent.getName());
//        }
//
//        if (newStudent.getAge() != null && !student.getAge().equals(newStudent.getAge())) {
//            student.setAge(newStudent.getAge());
//        }
//    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorMessage> handleException(NullPointerException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage(exception.getMessage()));
    }
}
