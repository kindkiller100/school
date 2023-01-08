package com.school.school.students;

import org.springdoc.api.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @GetMapping//добавить пагинацию?
    public List<Student> list() {
        return studentService.list();
    }
    @GetMapping("/{id}")
    public Student getById(@PathVariable long id){
        return studentService.getById(id);
    }
    @GetMapping("/deleted")
    public List<Student> getAllDeleted(){//нет валидации нa null(походу и не нужна)
        return studentService.getAllDeleted();
    }
    @GetMapping("/notDeleted")// перименовать в active ?
    public List<Student> getAllNotDeleted(){//нет валидации нa null(походу и не нужна)
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
    public void editById(@Valid @PathVariable long id, @RequestBody Student editStudent){
        studentService.editById(id,editStudent);
    }

    @ExceptionHandler({NullPointerException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorMessage> handleException(NullPointerException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage(exception.getMessage()));
    }
    public ResponseEntity<ErrorMessage> handleException(MethodArgumentNotValidException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessage(exception.getMessage()));
    }
}
