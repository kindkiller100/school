package com.school.school.lessons;

import com.school.school.utils.DateTimeRange;
import com.school.school.utils.LessonsCalculationsDtoIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/lessons")
public class LessonsController {
    @Autowired
    private LessonService lessonService;
    @Autowired
    private LessonDtoMapper lessonDtoInService;

    @GetMapping
    public List<Lesson> list()
    {
        return lessonService.list();
    }

    @GetMapping("/{id}")
    public Lesson getById(@PathVariable long id)
    {
        return lessonService.getIfExists(id);
    }

    @PostMapping("/date_range")
    public List<Lesson> getAllInDateRange(@RequestBody @Valid DateTimeRange dateRange)
    {
        return lessonService.getAllInDateRange(dateRange);
    }

    @GetMapping("/by_teacher/{id}")
    public List<Lesson> getAllByTeacherId(@PathVariable long id)
    {
        return lessonService.getAllByTeacherId(id);
    }

    @PostMapping("/by_teacher")
    public double countHoursOfLessonByTeacherInRange(@Valid @RequestBody LessonsCalculationsDtoIn lessonsCalc)
    {
        return lessonService.countHoursOfLessonsByTeacherInRange(lessonsCalc.getId(), lessonsCalc.getDateTimeRange());
    }

    @GetMapping("/by_student/{id}")
    public List<Lesson> getAllByStudentId(@PathVariable long id)
    {
        return lessonService.getAllByStudentId(id);
    }

    @PostMapping("/by_student")
    public double countHoursOfLessonsByStudentInRange(@Valid @RequestBody LessonsCalculationsDtoIn lessonsCalc)
    {
        return lessonService.countHoursOfLessonsByStudentInRange(lessonsCalc.getId(), lessonsCalc.getDateTimeRange());
    }

    @PostMapping
    public void create(@Valid @RequestBody LessonDtoIn lessonDto)
    {
        lessonService.create(lessonDtoInService.convertDtoToEntity(lessonDto, true));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id){
        lessonService.delete(id);
    }

    @PutMapping
    public void edit(@Valid @RequestBody LessonDtoIn editLessonDto){
        lessonService.edit(lessonDtoInService.convertDtoToEntity(editLessonDto, false));
    }
}
