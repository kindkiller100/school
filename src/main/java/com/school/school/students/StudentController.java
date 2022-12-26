package com.school.school.students;

import org.springdoc.api.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @GetMapping
    public List<Student> list() {
        return studentService.list();
    }

    @PostMapping
    public void create(@RequestBody Student student) {
        studentService.create(student);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        studentService.delete(id);
    }

    @PutMapping("/{id}")
    public void editById(@PathVariable long id, @RequestBody Student editStudent){
        studentService.editById(id,editStudent);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorMessage> handleException(NullPointerException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage(exception.getMessage()));
    }
}
