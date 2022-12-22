package com.school.school.subjects;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubjectService
{
    @Autowired
    private SubjectRepository subjectRepository;

    public List<Subject> list() {
        return subjectRepository.findAll();
    }

    public void create(Subject subject) {

        subjectRepository.save( subject );


    }
}
