package com.school.school.groups;

import com.school.school.exceptions.ValidationException;
import com.school.school.utils.PageableValidator;
import com.school.school.students.Student;
import com.school.school.students.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class GroupService {
    @Autowired
    GroupRepository repository;
    @Autowired
    StudentRepository studentRepository;

    public Page<Student> getAllStudentsInGroup(long id, Pageable pageable) {
        PageableValidator.sortValidOrThrow(Student.class, pageable);
        return studentRepository.getAllStudentsInGroup(id, pageable);
    }

    public Page<Group> getAll(Pageable pageable) {
        PageableValidator.sortValidOrThrow(Group.class, pageable);
        return repository.findAll(pageable);
    }

    public void create(Group group) {
        repository.save(group);
    }

    public void edit(Group group) {
        Group groupClone = getIfExists(group.getId())
                .clone()
                .setTitle(group.getTitle())
                .build();

        repository.save(groupClone);
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

    private Group getIfExists(long id) {
        return repository.getIfExists(id);
    }
}
