package com.school.school.lessons_groups;

import com.school.school.exceptions.ValidationException;
import com.school.school.lessons.LessonService;
import com.school.school.utils.PageableValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LessonsGroupService {
    @Autowired
    private LessonsGroupRepository repository;
    @Autowired
    private LessonService lessonService;

    public Page<LessonsGroup> list(Pageable pageable) {
        PageableValidator.checkIsSortValid(LessonsGroup.class, pageable);
        return repository.findAll(pageable);
    }

    public LessonsGroup getIfExists(long id) {
        return repository.getIfExists(id);
    }

    public void create(LessonsGroup lessonsGroup) {
        repository.save(lessonsGroup);
    }

    public void edit(LessonsGroup editLessonsGroup) {
        if (repository.existsById(editLessonsGroup.getId())) {
            repository.save(editLessonsGroup);
        } else {
            throw new ValidationException("id", "Группа занятий с id «" + editLessonsGroup.getId() + "» не найдена.")
                    .setStatus(HttpStatus.NOT_FOUND);
        }
    }

    public void delete(long id) {
        if (repository.existsById(id)) {
            LocalDateTime dateTime = LocalDateTime.now();
            lessonService.clearGroupIdBeforeDateTime(id, dateTime.plusSeconds(1));
            lessonService.deleteAllByGroupIdAndAfterDateTime(id, dateTime);
            repository.deleteById(id);
        } else {
            throw new ValidationException("id", "Группа занятий с id «" + id + "» не найдена.")
                    .setStatus(HttpStatus.NOT_FOUND);
        }
    }
}
