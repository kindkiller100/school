package com.school.school.teachers;

import com.school.school.lessons.LessonRepository;
import com.school.school.utils.PageableValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TeacherService {
    @Autowired
    private TeacherRepository repository;
    @Autowired
    private LessonRepository lessonRepository;

    public Teacher getIfExists(long id) {
        return repository.getIfExists(id);
    }

    public Page<Teacher> getAllDeleted(Pageable pageable) {
        PageableValidator.sortValidOrThrow(Teacher.class, pageable);
        return repository.findAllByDeletedIsTrue(pageable);
    }

    public Page<Teacher> getAll(Pageable pageable) {
        PageableValidator.sortValidOrThrow(Teacher.class, pageable);
        return repository.findAllByDeletedIsFalse(pageable);
    }

    public Page<Teacher> getAllByFilter(String like, Pageable pageable) {
        PageableValidator.sortValidOrThrow(Teacher.class, pageable);
        return repository.findAllByFilter(like, pageable);
    }

    public void create(Teacher teacher) {
        repository.save(teacher);
    }

    public void delete(long id) {
        Teacher teacher = getIfExists(id)
                .clone()
                .setDeleted(true)
                .build();
        repository.save(teacher);
    }

    //восстанавливает удаленного
    public void restoreDeleted(long id) {
        Teacher teacher = getIfExists(id)
                .clone()
                .setDeleted(false)
                .build();
        repository.save(teacher);
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
        repository.save(updatedTeacher);
    }

    //удаляет всех teacher с deleted == true,
    //на которых нет ссылок в связанной таблице lessons
    public void wipe() {
        repository.findAllByDeletedIsTrue(null).stream()
                .filter(teacher -> !lessonRepository.existsByTeacherId(teacher.getId()))
                .forEach(teacher -> repository.deleteById(teacher.getId()));
    }
}

