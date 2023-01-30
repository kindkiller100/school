package com.school.school.teachers;

import com.school.school.utils.PageableValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TeacherService {
    @Autowired
    private TeacherRepository teacherRepository;

    public Teacher getIfExists(long id) {
        return teacherRepository.getIfExists(id);
    }

    public Page<Teacher> getAllDeleted(Pageable pageable) {
        PageableValidator.checkIsSortValid(Teacher.class, pageable);
        return teacherRepository.findAllByDeletedIsTrue(pageable);
    }

    public Page<Teacher> getAll(Pageable pageable) {
        PageableValidator.checkIsSortValid(Teacher.class, pageable);
        return teacherRepository.findAllByDeletedIsFalse(pageable);
    }
    public Page<Teacher> getAllByFilter(String like, Pageable pageable) {
        PageableValidator.checkIsSortValid(Teacher.class, pageable);
        return teacherRepository.findAllByFilter(like, pageable);
    }
    public void create(Teacher teacher) {
        teacherRepository.save(teacher);
    }

    public void delete(long id) {
        Teacher teacher = getIfExists(id)
                .clone()
                .setDeleted(true)
                .build();
        teacherRepository.save(teacher);
    }

    //восстанавливает удаленного
    public void restoreDeleted(long id) {
        Teacher teacher = getIfExists(id)
                .clone()
                .setDeleted(false)
                .build();
        teacherRepository.save(teacher);
    }
    //редактируем запись
    public void edit(Teacher editTeacher) {
        long id = editTeacher.getId();
        //записываем значение deleted оригинального объекта из бд
        boolean isDeleted = getIfExists(id)
                .isDeleted();
        Teacher updatedTeacher = editTeacher
                .clone()
                .setDeleted(isDeleted)//перезаписываем значение deleted у обновленного объекта
                .build();
        teacherRepository.save(updatedTeacher);
    }
}

