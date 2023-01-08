package com.school.school.subjects;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

@Service
public class SubjectService
{
    @Autowired
    private SubjectRepository subjectRepository;

    public List<Subject> list() {
        return subjectRepository.findAll();
    }

    //возвращает список всех subject с установленным/снятым флагом deleted
    public List<Subject> getAll(boolean deleted) {
        return list().stream()
                .filter(subject -> subject.isDeleted() == deleted)
                .collect(Collectors.toList());
    }

    //создает subject
    public void create(Subject subject) {
        long id = subject.getId();
        String title = subject.getTitle();

        //TODO: change exceptions
        if (subjectRepository.existsById(id)) {
            throw new NotFoundException("Subject with id «" + id + "» already exists.");
        } else if (title.isEmpty()) {
            throw new NotFoundException("Title is empty.");
        } else if (existsByTitle(title)) {
            throw new NotFoundException("Subject with title «" + title + "» already exists.");
        } else if (subject.isDeleted()) {
            throw new NotFoundException("Checkbox «Deleted» cannot be true.");
        }

        subjectRepository.save(subject);
    }

    //изменяет title и description по id
    public void edit(Subject subject) {
        long id = subject.getId();
        String title = subject.getTitle();

        //TODO: change exceptions
        if (title.isEmpty()) {
            throw new NotFoundException("Title is empty.");
        } else if (existsByTitle(title, id)) {
            throw new NotFoundException("Subject with title «" + title + "» already exists.");
        }

        Subject subjectClone = subjectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Subject with id «" + id + "» not found."))
                .clone()
                .setTitle(title)
                .setDescription(subject.getDescription())
                .build();

        subjectRepository.save(subjectClone);
    }

    //устанавливает/снимает флаг deleted по id
    public void setDeleted(long id, boolean deleted) {
        //TODO: change exceptions
        Subject subjectClone = subjectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Subject with id «" + id + "» not found."))
                .clone()
                .setDeleted(deleted)
                .build();

        subjectRepository.save(subjectClone);
    }

    //возвращает subject по title
    private boolean existsByTitle(String title) {
        return list().stream()
                .anyMatch(subject -> subject.getTitle().equals(title));
    }

    //возвращает subject по title за исключением переданного id
    private boolean existsByTitle(String title, long excludedId) {
        return list().stream()
                .filter(subject -> subject.getId() != excludedId)
                .anyMatch(subject -> subject.getTitle().equals(title));
    }
}
