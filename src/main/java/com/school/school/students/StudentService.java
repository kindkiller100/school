package com.school.school.students;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.time.LocalDate;
import java.util.List;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;

    public List<Student> getAll() {
        return studentRepository.findAllByDeletedIsFalse();
    }

    public Student getIfExists(long id) {
        return studentRepository.getIfExists(id);
    }

    public List<Student> getAllDeleted() {
        return studentRepository.findAllByDeletedIsTrue();
    }


    public List<Student> getAllByFilter(String like) {
        return studentRepository.findAllByFilter(like);
    }

    public List<Student> getAllByAge(Byte fromAge, Byte uptoAge) {
        if (fromAge < 0 || uptoAge < 0 || fromAge > uptoAge) {
            throw new NumberFormatException("Wrong age period, from: " + fromAge + ", upto: " + uptoAge);
        }
        LocalDate fromDate = LocalDate.now().minusYears(uptoAge);
        LocalDate uptoDate = LocalDate.now().minusYears(fromAge);
        return studentRepository.findAllByDateOfBirthRange(fromDate, uptoDate);
    }


    public void create(Student student) {
        studentRepository.save(student);
    }

    public void delete(long id) {
        Student student = getIfExists(id);
        Student studentUpdated = student.clone().setDeleted(true).build();
        studentRepository.save(studentUpdated);
    }

    public void restoreDeleted(long id) {
        Student student = getIfExists(id);
        Student studentUpdated = student.clone().setDeleted(false).build();
        studentRepository.save(studentUpdated);
    }

    public void edit(Student editStudent) {
        long id = editStudent.getId();
        //записываем значение deleted оригинального объекта из бд
        boolean isDeleted = getIfExists(id).isDeleted();
        Student studentUpdated = editStudent
                .clone()
                .setDeleted(isDeleted)//перезаписываем значенеие deleted у обновленного объекта
                .build();
        studentRepository.save(studentUpdated);
    }
}
