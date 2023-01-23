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
    private LessonService service;
    @Autowired
    private LessonDtoMapper lessonDtoMapper;

    @GetMapping
    public List<Lesson> list()
    {
        return service.list();
    }

    @GetMapping("/{id}")
    public Lesson getById(@PathVariable long id)
    {
        return service.getIfExists(id);
    }

    @PostMapping("/date_range")
    public List<Lesson> getAllInDateRange(@RequestBody @Valid DateTimeRange dateRange)
    {
        return service.getAllInDateRange(dateRange);
    }

    @GetMapping("/by_teacher/{id}")
    public List<Lesson> getAllByTeacherId(@PathVariable long id)
    {
        return service.getAllByTeacherId(id);
    }

    @PostMapping("/by_teacher")
    public double countHoursOfLessonByTeacherInRange(@Valid @RequestBody LessonsCalculationsDtoIn lessonsCalc)
    {
        return service.countHoursOfLessonsByTeacherInRange(lessonsCalc.getId(), lessonsCalc.getDateTimeRange());
    }

    @GetMapping("/by_student/{id}")
    public List<Lesson> getAllByStudentId(@PathVariable long id)
    {
        return service.getAllByStudentId(id);
    }

    @PostMapping("/by_student")
    public double countHoursOfLessonsByStudentInRange(@Valid @RequestBody LessonsCalculationsDtoIn lessonsCalc)
    {
        return service.countHoursOfLessonsByStudentInRange(lessonsCalc.getId(), lessonsCalc.getDateTimeRange());
    }

    @PostMapping
    public void create(@Valid @RequestBody LessonDtoIn lessonDto)
    {
        service.create(lessonDtoMapper.convertDtoToEntity(lessonDto, true));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id){
        service.delete(id);
    }

    @PutMapping
    public void edit(@Valid @RequestBody LessonDtoIn editLessonDto){
        service.edit(lessonDtoMapper.convertDtoToEntity(editLessonDto, false));
    }
}
