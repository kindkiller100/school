package com.school.school.teachers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;

@Service
public class TeacherService {
    @Autowired
    private TeacherRepository teacherRepository;

    public Teacher getIfExists(long id) {
        return teacherRepository
                .findById(id)
                .orElseThrow(
                        () -> new NotFoundException("Teacher with id «" + id + "» not found")
                );
    }

    public List<Teacher> getAllDeleted() {
        return teacherRepository.findAllByDeletedIsTrue();
    }

    public List<Teacher> getAll() {
        return teacherRepository.findAllByDeletedIsFalse();
    }
    public List<Teacher> getAllByFilter(String like) {
        return teacherRepository.findAllByFilter(like);
    }
    public void create(Teacher teacher) {
        teacherRepository.save(teacher);
    }

    public void delete(long id) {
        Teacher st = teacherRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Student with id «" + id + "» not found"))
                .clone()
                .setDeleted(true)
                .build();
        teacherRepository.save(st);
    }

    //восстанавливает удаленного
    public void restoreDeleted(long id) {
        Teacher teacher = teacherRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Teacher with id «" + id + "» not found"))
                .clone()
                .setDeleted(false)
                .build();
        teacherRepository.save(teacher);
    }
    //редактируем запись
    public void edit(Teacher editTeacher) {
        long id = editTeacher.getId();
        //записываем значение deleted оригинального объекта из бд
        boolean isDeleted = teacherRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Teacher with id «" + id + "» not found"))
                .isDeleted();
        Teacher updatedStudent = editTeacher
                .clone()
                .setDeleted(isDeleted)//перезаписываем значение deleted у обновленного объекта
                .build();
        teacherRepository.save(updatedStudent);
    }
}

