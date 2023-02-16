package com.school.school.lessons;

import com.school.school.utils.DateRange;
import com.school.school.utils.LessonsCalculationsDtoIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/lessons")
public class LessonsController {
    @Autowired
    private LessonService service;
    @Autowired
    private LessonDtoMapper lessonDtoMapper;

    @GetMapping
    public Page<Lesson> list(Pageable pageable)
    {
        return service.list(pageable);
    }

    @GetMapping("/{id}")
    public Lesson getById(@PathVariable long id)
    {
        return service.getIfExists(id);
    }

    @PostMapping("/date_range")
    public Page<Lesson> getAllInDateRange(@RequestBody DateRange dateRange, Pageable pageable)
    {
        return service.getAllInDateRange(dateRange, pageable);
    }

    @GetMapping("/by_teacher/{id}")
    public Page<Lesson> getAllByTeacherId(@PathVariable long id, Pageable pageable)
    {
        return service.getAllByTeacherId(id, pageable);
    }

    @PostMapping("/by_teacher")
    public double countHoursOfLessonByTeacherInRange(@Valid @RequestBody LessonsCalculationsDtoIn lessonsCalc)
    {
        return service.countHoursOfLessonsByTeacherInRange(lessonsCalc.getId(), lessonsCalc.getDateTimeRange());
    }

    @GetMapping("/by_student/{id}")
    public Page<Lesson> getAllByStudentId(@PathVariable long id, Pageable pageable)
    {
        return service.getAllByStudentId(id, pageable);
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
