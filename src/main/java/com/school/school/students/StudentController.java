package com.school.school.students;

import org.springdoc.api.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<Student> list() {
        return studentService.list();
    }
    @GetMapping("/{id}")
    public Student getById(@PathVariable long id){
        return studentService.getById(id);
    }
    @GetMapping("/deleted")
    public List<Student> getAllDeleted(){
        return studentService.getAllDeleted();
    }
    @GetMapping("/notDeleted")// перименовать в active ?
    public List<Student> getAllNotDeleted(){
        return studentService.getAllNotDeleted();
    }

    @PostMapping
    public void create(@Valid @RequestBody Student student) {
        studentService.create(student);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        studentService.delete(id);
    }

    @PutMapping("/{id}/restore")// или DeleteMapping?
    public void restoreDeleted(@PathVariable long id) {
        studentService.restoreDeleted(id);//нет валидации нa null
    }
    @PutMapping("/{id}/edit")
    public void editById(@Valid @RequestBody Student editStudent){
        studentService.editById(editStudent);
    }

    @ExceptionHandler({NullPointerException.class})
    public ResponseEntity<ErrorMessage> handleException(NullPointerException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage(exception.getMessage()));
    }
    //обработка исключений валидации
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
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
