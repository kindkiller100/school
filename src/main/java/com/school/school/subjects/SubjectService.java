package com.school.school.subjects;

import com.school.school.exceptions.ValidationException;
import com.school.school.utils.PageableValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SubjectService
{
    @Autowired
    private SubjectRepository subjectRepository;

    //возвращает список всех subject с deleted == false
    public Page<Subject> getAll(Pageable pageable) {
        PageableValidator.checkIsSortValid(Subject.class, pageable);
        return subjectRepository.findAllByDeletedIsFalse(pageable);
    }

    //возвращает список всех subject с deleted == true
    public Page<Subject> getAllDeleted(Pageable pageable) {
        PageableValidator.checkIsSortValid(Subject.class, pageable);
        return subjectRepository.findAllByDeletedIsTrue(pageable);
    }
    public Subject getIfExists(long id) {
        return subjectRepository.getIfExists(id);
    }

    //создает subject
    public void create(Subject subject) {
        long id = subject.getId();
        String title = subject.getTitle();
        ValidationException validationException = new ValidationException();

        if (subjectRepository.existsById(id)) {
            validationException.put("id", "Предмет с id «" + id + "» уже существует.");
        }

        if (subjectRepository.existsByTitle(title)) {
            validationException.put("title", "Предмет с заголовком «" + title + "» уже существует.");
        }

        validationException.throwExceptionIfIsNotEmpty();

        subjectRepository.save(subject);
    }

    //изменяет title и description по id
    public void edit(Subject subject) {
        long id = subject.getId();
        String title = subject.getTitle();
        ValidationException validationException = new ValidationException();

        if (subjectRepository.existsByTitleAndIdNot(title, id)) {
            validationException.put("title", "Предмет с заголовком «" + title + "» уже существует.");
        }

        Subject subjectClone = subjectRepository.getIfExists(id)
                .clone()
                .setTitle(title)
                .setDescription(subject.getDescription())
                .build();

        subjectRepository.save(subjectClone);
    }

    //устанавливает флаг deleted по id
    public void delete(long id) {
        setDeletedById(id, true);
    }

    //снимает флаг deleted по id
    public void restoreDeleted(long id) {
        setDeletedById(id, false);
    }

    //устанавливает/снимает флаг deleted по id
    private void setDeletedById(long id, boolean deleted) {
        Subject subjectClone = subjectRepository.getIfExists(id)
                .clone()
                .setDeleted(deleted)
                .build();

        subjectRepository.save(subjectClone);
    }
}
