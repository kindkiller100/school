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
    @GetMapping("/filter/{like}")
    //поиск по частичному совпадению строки like в колонках name, secondname, lastname, telephonenumber
    public List<Student> getAllByFilter(@PathVariable String like){
        return studentService.getAllByFilter(like);
    }
    @GetMapping("/filterByAge/{from}/{upto}")//нет валидации на отрицательные числа
    //поиск по возрасту "от" и "до", в годах
    public List<Student> getAllByAge(@PathVariable Byte from, @PathVariable Byte upto){
        return studentService.getAllByAge(from, upto);
    }

    @PostMapping //можно создавать сразу удаленных
    public void create(@Valid @RequestBody Student student) {
        studentService.create(student);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        studentService.delete(id);
    }

    @PutMapping("/{id}/restore")// или DeleteMapping?
    public void restoreDeleted(@PathVariable long id) {
        studentService.restoreDeleted(id);
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
