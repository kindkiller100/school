package com.school.school.teachers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;

@Service
public class TeacherService
{
    @Autowired
    private TeacherRepository teacherRepository;
    public List<Teacher> list() {
        return teacherRepository.findAll();
    }
    public void create(Teacher teacher) {
        teacherRepository.save(teacher);
    }

    public void delete(long id) {
        if (teacherRepository.existsById(id)) {
            teacherRepository.deleteById(id);
        }
        else {
            throw new NotFoundException("Teacher with id " + id + " not found.");
        }
    }

    public void editById(long id, Teacher editTeacher) {
        if (teacherRepository.existsById(editTeacher.getId())) {
            teacherRepository.save(editTeacher);
        } else {
            throw new NotFoundException("Teacher with id " + id + " not found");
        }
    }
}

