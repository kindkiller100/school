package com.school.school.students;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;

    public List<Student> list() {
        return studentRepository.findAll();
    }

    public Student getById(long id) {
        return studentRepository
                .findById(id)
                .orElseThrow(
                        () -> new NotFoundException("Student with id «" + id + "» not found")
                );
    }

    public List<Student> getAllDeleted() {
        return studentRepository.findAllByDeletedIsTrue();
    }

    public List<Student> getAllNotDeleted() {
        return studentRepository.findAllByDeletedIsFalse();
    }

    public void create(Student student) {
        studentRepository.save(student);
    }

    public void delete(long id) {
        if (studentRepository.existsById(id)) {
            Student st = studentRepository
                    .findById(id)
                    .orElseThrow(() -> new NotFoundException("Student with id «" + id + "» not found"))
                    .clone()
                    .setDeleted(true)
                    .build();
            studentRepository.save(st);
        }
    }

    public void restoreDeleted(long id) {
        if (studentRepository.existsById(id)) {
            Student st = studentRepository
                    .findById(id)
                    .orElseThrow(() -> new NotFoundException("Student with id «" + id + "» not found"))
                    .clone()
                    .setDeleted(false)
                    .build();
            studentRepository.save(st);
        }
    }

    public void editById(Student editStudent) {
        long id = editStudent.getId();
        Student updatedStudent = editStudent
                .clone()
                .setDeleted(studentRepository
                        .findById(id)
                        .orElseThrow(() -> new NotFoundException("Student with id «" + id + "» not found"))
                        .isDeleted())
                .build();
        studentRepository.save(updatedStudent);
    }

}
