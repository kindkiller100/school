package com.school.school.lessons_groups;

import com.school.school.exceptions.ValidationException;
import com.school.school.utils.PageableValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class LessonsGroupsService {
    @Autowired
    private LessonsGroupsRepository lessonsGroupsRepository;

    public Page<LessonsGroups> list(Pageable pageable) {
        PageableValidator.checkIsSortValid(LessonsGroups.class, pageable);
        return lessonsGroupsRepository.findAll(pageable);
    }

    public LessonsGroups getIfExists(long id) {
        return lessonsGroupsRepository.getIfExists(id);
    }

    public void create(LessonsGroups lessonsGroups) {
        lessonsGroupsRepository.save(lessonsGroups);
    }

    public void edit(LessonsGroups editLessonsGroups) {
        if (lessonsGroupsRepository.existsById(editLessonsGroups.getId())) {
            lessonsGroupsRepository.save(editLessonsGroups);
        } else {
            throw new ValidationException("id", "Группа занятий с id «" + editLessonsGroups.getId() + "» не найдена.")
                    .setStatus(HttpStatus.NOT_FOUND);
        }
    }

    public void delete(long id) {
        if (lessonsGroupsRepository.existsById(id)) {
            lessonsGroupsRepository.deleteById(id);
        } else {
            throw new ValidationException("id", "Группа занятий с id «" + id + "» не найдена.")
                    .setStatus(HttpStatus.NOT_FOUND);
        }
    }
}
