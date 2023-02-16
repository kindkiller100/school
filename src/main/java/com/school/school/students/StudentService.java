package com.school.school.students;

import com.school.school.exceptions.ValidationException;
import com.school.school.utils.PageableValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


@Service
public class StudentService {
    @Autowired
    private StudentRepository repository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private StudentsGroupRepository studentsGroupRepository;
    @Autowired
    private LessonRepository lessonRepository;

    public Page<Student> getAll(Pageable pageable) {
        PageableValidator.sortValidOrThrow(Student.class, pageable);
        return repository.findAllByDeletedIsFalse(pageable);
    }

    public Student getIfExists(long id) {
        return repository.getIfExists(id);
    }

    public Page<Student> getAllDeleted(Pageable pageable) {
        PageableValidator.sortValidOrThrow(Student.class, pageable);
        return repository.findAllByDeletedIsTrue(pageable);
    }


    public Page<Student> getAllByFilter(String like, Pageable pageable) {
        PageableValidator.sortValidOrThrow(Student.class, pageable);
        return repository.findAllByFilter(like, pageable);
    }

    public Page<Student> getAllByAge(Byte fromAge, Byte uptoAge, Pageable pageable) {
        ValidationException validationException = new ValidationException();
        validationException.put(PageableValidator.sortValid(Student.class, pageable));
        if (fromAge < 0 || uptoAge < 0 || fromAge > uptoAge) {
            validationException.put("age", "Неверный период, от: " + fromAge + ", до: " + uptoAge);
        }
        validationException.throwExceptionIfIsNotEmpty();
        LocalDate fromDate = LocalDate.now().minusYears(uptoAge);
        LocalDate uptoDate = LocalDate.now().minusYears(fromAge);
        return repository.findAllByDateOfBirthRange(fromDate, uptoDate, pageable);
    }


    public void create(Student student) {
        repository.save(student);
    }

    public void delete(long id) {
        Student student = getIfExists(id);
        Student studentUpdated = student.clone().setDeleted(true).build();
        repository.save(studentUpdated);
    }

    public void restoreDeleted(long id) {
        Student student = getIfExists(id);
        Student studentUpdated = student.clone().setDeleted(false).build();
        repository.save(studentUpdated);
    }

    public void edit(Student editStudent) {
        long id = editStudent.getId();
        //записываем значение deleted оригинального объекта из бд
        boolean isDeleted = getIfExists(id).isDeleted();
        Student studentUpdated = editStudent
                .clone()
                .setDeleted(isDeleted)//перезаписываем значение deleted у обновленного объекта
                .build();
        repository.save(studentUpdated);
    }

    //удаляет всех student с deleted == true, на которых нет ссылок в связанных таблицах
    // student_lesson, students_groups_students и payments
    public void wipe() {
        repository.findAllByDeletedIsTrue(null).stream()
                .filter(student ->
                        studentsGroupRepository.countStudentIdInGroups(student.getId()) == 0 &&
                            lessonRepository.countStudentIdInLessons(student.getId()) == 0 &&
                                !paymentRepository.existsByStudentId(student.getId()))
                .forEach(student -> repository.deleteById(student.getId()));
    }
}
