package com.school.school.students_groups;

import com.school.school.exceptions.ValidationException;
import com.school.school.students.Student;
import com.school.school.students.StudentRepository;
import com.school.school.utils.PageableValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class StudentsGroupService {
    @Autowired
    StudentsGroupRepository repository;
    @Autowired
    StudentRepository studentRepository;

    public Page<Student> getAllStudentsInGroup(long id, Pageable pageable) {
        PageableValidator.checkIsSortValid(Student.class, pageable);
        return studentRepository.getAllStudentsInGroup(id, pageable);
    }


    public Page<StudentsGroup> getAll(Pageable pageable) {
        PageableValidator.checkIsSortValid(StudentsGroup.class, pageable);
        return repository.findAll(pageable);
    }

    public void create(StudentsGroup studentsGroup) {
        repository.save(studentsGroup);
    }

    public void edit(StudentsGroup studentsGroup) {
        StudentsGroup studentsGroupClone = getIfExists(studentsGroup.getId())
                .clone()
                .setTitle(studentsGroup.getTitle())
                .build();

        repository.save(studentsGroupClone);
    }

    public void delete(long id) {
        checkIfExists(id);
        repository.deleteById(id);
    }

    private void checkIfExists(long id) {
        if (!repository.existsById(id)) {
            throw new ValidationException("id", "Группа с id «" + id + "» не найдена.").setStatus(HttpStatus.NOT_FOUND);
        }
    }

    private StudentsGroup getIfExists(long id) {
        return repository.getIfExists(id);
    }
}
