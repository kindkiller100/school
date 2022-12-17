package com.school.school.students;

import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/students")
public class StudentsController {
    @GetMapping
    public List<Student> getAll(){
        return StudentsStorage.data;
    }

    @PostMapping
    public Student create(@RequestBody Student student){
        if (student.getId() == null) {
            student.generateId();
        }
        StudentsStorage.data.add(student);
        return student;
    }

    @GetMapping("/{id}")
    public Student getById(@PathVariable UUID id){
        return getStudentById(id);
    }

    @PutMapping("/{id}")
    public Student put(@PathVariable UUID id,
                       @RequestBody Student newStudent){
        Student student = getStudentById(id);
        if (newStudent.getName() != null) {
            student.setName(newStudent.getName());
        }
        if (newStudent.getAge() != null) {
            student.setAge(newStudent.getAge());
        }
        return student;
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable UUID id){

        StudentsStorage.data.removeIf(student -> student.getId().equals(id));
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorMessage> handleException(NullPointerException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage(exception.getMessage()));
    }

    private Student getStudentById(UUID id){
        if (id != null){
            Optional<Student> optionalStudent = StudentsStorage.data
                    .stream()
                    .filter(s -> s.getId().equals(id))
                    .findFirst();
            if (optionalStudent.isPresent()) {
                return optionalStudent.get();
            } else {
                throw new NullPointerException("Don`t found a student by id.");
            }
        } else
            throw new NullPointerException("Student`s id is empty.");
    }
}
