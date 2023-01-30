package com.school.school.students_groups;

import com.school.school.exceptions.ValidationException;
import com.school.school.utils.PageableValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class StudentsGroupService {
    @Autowired
    StudentsGroupRepository studentsGroupRepository;

    public Page<StudentsGroup> getAll(Pageable pageable) {
        PageableValidator.checkIsSortValid(StudentsGroup.class, pageable);
        return studentsGroupRepository.findAll(pageable);
    }

    public void create(StudentsGroup studentsGroup) {
        studentsGroupRepository.save(studentsGroup);
    }

    public void edit(StudentsGroup studentsGroup) {
        StudentsGroup studentsGroupClone = getIfExists(studentsGroup.getId())
                .clone()
                .setTitle(studentsGroup.getTitle())
                .build();

        studentsGroupRepository.save(studentsGroupClone);
    }

    public void delete(long id) {
        checkIfExists(id);
        studentsGroupRepository.deleteById(id);
    }

    private void checkIfExists(long id) {
        if (!studentsGroupRepository.existsById(id)) {
            throw new ValidationException("id", "Группа с id «" + id + "» не найдена.").setStatus(HttpStatus.NOT_FOUND);
        }
    }

    private StudentsGroup getIfExists(long id) {
        return studentsGroupRepository.getIfExists(id);
    }
}
