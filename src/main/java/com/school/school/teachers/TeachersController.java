package com.school.school.teachers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public List<Teacher> getAll() {
        return teacherService.getAll();
    }

    @GetMapping("/deleted")
    public List<Teacher> getAllDeleted() {
        return teacherService.getAllDeleted();
    }

    @GetMapping("/filter/{like}")
    public List<Teacher> getAllByFilter(@PathVariable String like) {
        return teacherService.getAllByFilter(like);
    }

    @PostMapping
    public void create(@Valid @RequestBody Teacher teacher) {
        teacherService.create(teacher);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        teacherService.delete(id);
    }

    @PutMapping("/edit")
    public void edit(@Valid @RequestBody Teacher editTeacher) {
        teacherService.edit(editTeacher);
    }

    @PutMapping("/{id}/restore")
    public void restoreDeleted(@PathVariable long id) {
        teacherService.restoreDeleted(id);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleException(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
