package com.school.school.students;

import com.school.school.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;

    public Page<Student> getAll(Pageable pageable) {

        Sort sort = pageable.getSort();
        //работает, перенести в метод
        //сделать универсальным?
        try {//+коммент
            for (Sort.Order order : sort) {
                String property = order.getProperty();
                Field field = Student.class.getDeclaredField(property);
            }
        } catch (NoSuchFieldException e) {
            String fieldName = e.getMessage();
            throw new ValidationException(fieldName,
                    "Неверные параметры сортировки, поля c именем " + fieldName + " не существует.");
        }
        return studentRepository.findAllByDeletedIsFalse(pageable);
    }

    public Student getIfExists(long id) {
        return studentRepository.getIfExists(id);
    }

    public Page<Student> getAllDeleted(Pageable pageable) {
        return studentRepository.findAllByDeletedIsTrue(pageable);
    }


    public Page<Student> getAllByFilter(String like, Pageable pageable) {
        return studentRepository.findAllByFilter(like, pageable);
    }

    public List<Student> getAllByAge(Byte fromAge, Byte uptoAge) {
        if (fromAge < 0 || uptoAge < 0 || fromAge > uptoAge) {
            throw new ValidationException("age", "Неверный период, от: " + fromAge + ", до: " + uptoAge);
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
