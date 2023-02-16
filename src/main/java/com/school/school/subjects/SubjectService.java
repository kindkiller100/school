package com.school.school.subjects;

import com.school.school.exceptions.ValidationException;
import com.school.school.lessons.LessonRepository;
import com.school.school.utils.PageableValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectService {
    @Autowired
    private SubjectRepository repository;
    @Autowired
    private LessonRepository lessonRepository;

    //возвращает список всех subject с deleted == false
    public Page<Subject> getAll(Pageable pageable) {
        PageableValidator.checkIsSortValid(Subject.class, pageable);
        return repository.findAllByDeletedIsFalse(pageable);
    }

    //возвращает список всех subject с deleted == true
    public Page<Subject> getAllDeleted(Pageable pageable) {
        PageableValidator.checkIsSortValid(Subject.class, pageable);
        return repository.findAllByDeletedIsTrue(pageable);
    }

    public Subject getIfExists(long id) {
        return repository.getIfExists(id);
    }

    //создает subject
    public void create(Subject subject) {
        long id = subject.getId();
        String title = subject.getTitle();
        ValidationException validationException = new ValidationException();

        if (repository.existsById(id)) {
            validationException.put("id", "Предмет с id «" + id + "» уже существует.");
        }

        if (repository.existsByTitleAndDeletedFalse(title) ) {
        if (repository.existsByTitle(title)) {
            validationException.put("title", "Предмет с заголовком «" + title + "» уже существует.");
        }

        validationException.throwExceptionIfIsNotEmpty();

        repository.save(subject);
    }

    //изменяет title и description по id
    public void edit(Subject subject) {
        long id = subject.getId();
        String title = subject.getTitle();
        ValidationException validationException = new ValidationException();

        if (repository.existsByTitleAndIdNotAndDeletedFalse(title, id)) {
            validationException.put("title", "Предмет с заголовком «" + title + "» уже существует.");
        }

        Subject subjectClone = repository.getIfExists(id)
                .clone()
                .setTitle(title)
                .setDescription(subject.getDescription())
                .build();

        repository.save(subjectClone);
        validationException.throwExceptionIfIsNotEmpty();
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
        Subject subjectClone = repository.getIfExists(id)
                .clone()
                .setDeleted(deleted)
                .build();

        repository.save(subjectClone);
    }
//удаляет все subject с deleted == true,
// на которые нет ссылок в связанной таблице lessons
     public void wipe() {
     repository.findAllByDeletedIsTrue(null).stream()
        .filter(subject -> !lessonRepository.existsBySubjectId(subject.getId()))
        .forEach(subject -> repository.deleteById(subject.getId()));
}
