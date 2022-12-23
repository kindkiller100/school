package com.school.school.lessons;

import com.school.school.subjects.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lessons")
public class LessonsController {
    @Autowired
    LessonService lessonService;

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

    @PutMapping("/{id}")
    public void editById(@PathVariable long id, @RequestBody Lesson editLesson){
        lessonService.editById(id, editLesson);
    }
}
