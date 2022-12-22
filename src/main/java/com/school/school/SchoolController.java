package com.school.school;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.school.school.subjects.SubjectRepository;

@RestController
public class SchoolController {

    final
    SubjectRepository subjectRepository;


    public SchoolController( SubjectRepository subjectRepository )
    {
        this.subjectRepository = subjectRepository;
    }


    @GetMapping("/{name}/{age}/{strength}")
    public String home(
            @PathVariable String name,
            @PathVariable("age") Integer temp,
            @PathVariable(value = "strength", required = false) Integer strength
    ) {
        String s = strength == null ? "" : strength.toString();
        return String.format("Hello, %s!", name + "age= " + temp + s);
    }
}
