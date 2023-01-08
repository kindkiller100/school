package com.school.school.subjects;

import java.util.List;

import org.springdoc.api.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.webjars.NotFoundException;

@RestController
@RequestMapping("/subjects")
public class SubjectController {

    @Autowired
    SubjectService subjectService;

    @GetMapping
    public List<Subject> list() {
        return subjectService.list();
    }

    @GetMapping("/{deleted}")
    public List<Subject> getAll(@PathVariable boolean deleted) {
        return subjectService.getAll(deleted);
    }

    @PostMapping
    public void create(@RequestBody Subject subject) {
        subjectService.create(subject);
    }

    @PutMapping
    public void edit(@RequestBody Subject subject) {
        subjectService.edit(subject);
    }

    @DeleteMapping("/{id}/{deleted}")
    public void delete(@PathVariable long id, @PathVariable boolean deleted) {
        subjectService.setDeleted(id, deleted);
    }

    //метод, который отправляет на UI сообщение об ошибке
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorMessage> handleException(NotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage(exception.getMessage()));
    }
}
