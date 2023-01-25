package com.school.school.students;

import org.springdoc.api.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/students")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @GetMapping
    public Page<Student> getAll(Pageable pageable){
        return studentService.getAll(pageable);
    }
    @GetMapping("/{id}")
    public Student getById(@PathVariable long id){
        return studentService.getIfExists(id);
    }
    @GetMapping("/deleted")
    public Page<Student> getAllDeleted(Pageable pageable){
        return studentService.getAllDeleted(pageable);
    }
    @GetMapping("/filter/{like}")
    //поиск по частичному совпадению строки like в колонках name, secondname, lastname, telephonenumber
    public Page<Student> getAllByFilter(@PathVariable String like, Pageable pageable){
        return studentService.getAllByFilter(like, pageable);
    }
    @GetMapping("/filter/age/{from}/{upto}")
    //поиск по возрасту "от" и "до", в годах
    public Page<Student> getAllByAge(@PathVariable Byte from, @PathVariable Byte upto, Pageable pageable){
        return studentService.getAllByAge(from, upto, pageable);
    }

    @PostMapping
    public void create(@Valid @RequestBody Student student) {
        studentService.create(student);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        studentService.delete(id);
    }

    @PutMapping("/{id}/restore")
    public void restoreDeleted(@PathVariable long id) {
        studentService.restoreDeleted(id);
    }
    @PutMapping("/edit")
    public void editById(@Valid @RequestBody Student editStudent){
        studentService.edit(editStudent);
    }

}
