package com.school.school.subjects;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/subjects")
public class SubjectsController {

    @Autowired
    SubjectService subjectService;

    @GetMapping
    public List<Subject> list()
    {
        return subjectService.list();
    }

    @PostMapping
    public void create( @RequestBody Subject subject )
    {
        subjectService.create(subject);
    }
}
