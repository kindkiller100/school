package com.school.school.lessons;

import com.school.school.utils.DateTimeRange;
import com.school.school.utils.LessonsCalculationsDtoIn;
import org.springdoc.api.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/lessons")
public class LessonsController {
    @Autowired
    private LessonService lessonService;

    @GetMapping
    public List<Lesson> list()
    {
        return lessonService.list();
    }

    @GetMapping("/{id}")
    public Lesson getById(@PathVariable long id)
    {
        return lessonService.getById(id);
    }

    @PostMapping("/date_range") //? GetMapping
    public List<Lesson> getAllInDateRange(@RequestBody @Valid DateTimeRange dateRange)
    {
        return lessonService.getAllInDateRange(dateRange);
    }

    @GetMapping("/by_teacher/{id}")
    public List<Lesson> getAllByTeacherId(@PathVariable long id)
    {
        return lessonService.getAllByTeacherId(id);
    }

    @PostMapping("/by_teacher/")    //? GetMapping
    public double countHoursOfLessonByTeacherInRange(@RequestBody @Valid LessonsCalculationsDtoIn lessonsCalc)
    {
        return lessonService.countHoursOfLessonsByTeacherInRange(lessonsCalc);
    }

    @GetMapping("/by_student/{id}")
    public List<Lesson> getAllByStudentId(@PathVariable long id)
    {
        return lessonService.getAllByStudentId(id);
    }

    @PostMapping("/by_student") //? GetMapping
    public double countHoursOfLessonsByStudentInRange(@RequestBody @Valid LessonsCalculationsDtoIn lessonsCalc)
    {
        return lessonService.countHoursOfLessonsByStudentInRange(lessonsCalc);
    }

    @PostMapping
    public void create(@RequestBody @Valid Lesson lesson)
    {
        lessonService.create(lesson);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id){
        lessonService.delete(id);
    }

    @PutMapping
    public void editById(@RequestBody @Valid Lesson editLesson){
        lessonService.editById(editLesson);
    }

    //метод, который отправляем на UI сообщение об ошибке
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorMessage> handleException(NotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage(exception.getMessage()));
    }

    //метод, который отправляет на UI сообщение об ошибках валидаций объектов
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                errors.put(fieldName, errorMessage);
            });
        return errors;
    }
}
