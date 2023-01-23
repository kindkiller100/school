package com.school.school.subjects;

import java.util.List;

import com.school.school.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

@Service
public class SubjectService
{
    @Autowired
    private SubjectRepository subjectRepository;

    //возвращает список всех subject с deleted == false
    public List<Subject> getAll() {
        return subjectRepository.findAllByDeletedIsFalse();
    }

    //возвращает список всех subject с deleted == true
    public List<Subject> getAllDeleted() {
        return subjectRepository.findAllByDeletedIsTrue();
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

        validationException.throwExceptionIfIsNotEmpty();

        //TODO: change to getIfExists()
        Subject subjectClone = subjectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Предмет с id «" + id + "» не найден."))
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
        //TODO: change to getIfExists()
        Subject subjectClone = subjectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Предмет с id «" + id + "» не найден."))
                .clone()
                .setDeleted(deleted)
                .build();

        subjectRepository.save(subjectClone);
    }
}
