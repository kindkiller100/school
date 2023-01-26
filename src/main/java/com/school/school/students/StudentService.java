package com.school.school.students;

import com.school.school.exceptions.ValidationException;
import com.school.school.utils.PageableValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.time.LocalDate;


@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;

    public Page<Student> getAll(Pageable pageable) {
        PageableValidator.checkIsSortValid(Student.class, pageable);
        return studentRepository.findAllByDeletedIsFalse(pageable);
    }

    public Student getIfExists(long id) {
        return studentRepository.getIfExists(id);
    }

    public Page<Student> getAllDeleted(Pageable pageable) {
        PageableValidator.checkIsSortValid(Student.class, pageable);
        return studentRepository.findAllByDeletedIsTrue(pageable);
    }


    public Page<Student> getAllByFilter(String like, Pageable pageable) {
        PageableValidator.checkIsSortValid(Student.class, pageable);
        return studentRepository.findAllByFilter(like, pageable);
    }

    public Page<Student> getAllByAge(Byte fromAge, Byte uptoAge, Pageable pageable) {
        ValidationException validationException = new ValidationException();
        if( ! PageableValidator.isSortValid(Student.class, pageable) )
        {
            validationException.put( "pageable", PageableValidator.currentError );
        }
        if (fromAge < 0 || uptoAge < 0 || fromAge > uptoAge) {
            validationException.put("age", "Неверный период, от: " + fromAge + ", до: " + uptoAge);
        }
        validationException.throwExceptionIfIsNotEmpty();
        LocalDate fromDate = LocalDate.now().minusYears(uptoAge);
        LocalDate uptoDate = LocalDate.now().minusYears(fromAge);
        return studentRepository.findAllByDateOfBirthRange(fromDate, uptoDate, pageable);
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
