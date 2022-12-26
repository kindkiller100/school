package com.school.school.lessons;

import org.springdoc.api.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;

import java.util.List;

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

    @PostMapping
    public void create(@RequestBody Lesson lesson)
    {
        lessonService.create(lesson);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id){
        lessonService.delete(id);
    }

    @PutMapping
    public void editById( @RequestBody Lesson editLesson){
        lessonService.editById(editLesson);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorMessage> handleException(NotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage(exception.getMessage()));
    }
}
