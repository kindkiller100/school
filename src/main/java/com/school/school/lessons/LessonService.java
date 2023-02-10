package com.school.school.lessons;

import com.school.school.exceptions.ValidationException;
import com.school.school.students.StudentRepository;
import com.school.school.subjects.SubjectRepository;
import com.school.school.teachers.TeacherRepository;
import com.school.school.utils.DateTimeRange;
import com.school.school.utils.PageableValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class LessonService {
    @Autowired
    private LessonRepository repository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private SubjectRepository subjectRepository;

    //получить все занятия
    public Page<Lesson> list(Pageable pageable){
        PageableValidator.checkIsSortValid(Lesson.class, pageable);
        return repository.findAll(pageable);
    }

    //получить занятие по id
    public Lesson getIfExists(long id){
        return repository.getIfExists(id);
    }

    //получить все занятия из диапазона дат
    public Page<Lesson> getAllInDateRange(DateTimeRange dateRange, Pageable pageable){
        ValidationException validationException = new ValidationException();

        if(!PageableValidator.isSortValid(Lesson.class, pageable)) {
            validationException.put("pageable", PageableValidator.currentError);
        }

        //проверка, что начало диапазона меньше или равно концу диапазона
        if(!dateRange.isValid()) {
            validationException.put("date_range", DateTimeRange.ERR_STRING);
        }

        validationException.throwExceptionIfIsNotEmpty();

        return repository.findLessonsByStartDateTimeBetween(dateRange.getFrom(), dateRange.getTo(), pageable);
    }

    //получить все занятия по id преподавателя
    public Page<Lesson> getAllByTeacherId(long id, Pageable pageable) {
        PageableValidator.checkIsSortValid(Lesson.class, pageable);
        return repository.findLessonsByTeacherId(id, pageable);
    }

    //получить все занятия по id студента
    public Page<Lesson> getAllByStudentId(long id, Pageable pageable) {
        PageableValidator.checkIsSortValid(Lesson.class, pageable);
        return repository.findLessonsByStudentId(id, pageable);
    }

    //количество часов занятий, проведенных преподавателем за период
    public double countHoursOfLessonsByTeacherInRange(long id, DateTimeRange dateTimeRange) {
        ValidationException validationException = new ValidationException();

        //проверяем, что записть с таким Id существует
        if (!teacherRepository.existsById(id)) {
            validationException.put("id", "Преподаватель с id «" + id + "» не найден.");
        }
        // проверка диапазона дат
        if (!dateTimeRange.isValid()) {
            validationException.put("date_time_range", DateTimeRange.ERR_STRING);
        }

        validationException.throwExceptionIfIsNotEmpty();

        //получаем количество минут занятий, проведенных преподавателем за период, и переводим в часы
        return repository.countDurationOfLessonsByTeacherInRange(id, dateTimeRange.getFrom(), dateTimeRange.getTo())/60d;
    }

    //количество часов занятий, посещенных студентом за период
    public double countHoursOfLessonsByStudentInRange(long id, DateTimeRange dateTimeRange) {
        ValidationException validationException = new ValidationException();

        //проверяем, что запись с таким Id существует
        if (!studentRepository.existsById(id)) {
            validationException.put("id", "Студент с id «" + id + "» не найден.");
        }
        // проверка диапазона дат
        if (!dateTimeRange.isValid()) {
            validationException.put("date_time_range", DateTimeRange.ERR_STRING);
        }

        validationException.throwExceptionIfIsNotEmpty();

        //получаем количество минут занятий, посещенных студеном за период, и переводим в часы
        return repository.findDurationByStudentIdInRange(dateTimeRange.getFrom(), dateTimeRange.getTo(), id)/ 60d;
    }

    //создание занятия
    //параметр lesson содержит поля subject и teacher, у которых заполнен только id (после маппинга из LessonDtoIn)
    public void create(Lesson lesson){
        validate(lesson, false);
        repository.save(lesson);
    }

    //удаление занятия по id
    public void delete(long id){
        if (repository.existsById(id)) {      //проверяем, есть ли запись с таким id в базе данных
            repository.deleteById(id);        //удаляем запись по id
        } else {                                    //если записи нет - выбрасываем ошибку
            throw new ValidationException("id", "Занятие с id «" + id + "» не найдено.").setStatus(HttpStatus.NOT_FOUND);
        }
    }

    //редактирование занятия
    //параметр editLesson содержит поля subject и teacher, у которых заполнен только id (после маппинга из LessonDtoIn)
    public void edit(Lesson editLesson){
        validate(editLesson, true);
        //сохраняем запись с измененными данными в БД
        repository.save(editLesson);
    }

    private void validate(Lesson lesson, boolean editFlag) {
        ValidationException validationException = new ValidationException();

        //проверяем, есть ли запись с таким id в базе данных
        if (editFlag && !repository.existsById(lesson.getId())) {
            validationException.put("id", "Занятие с id «" + lesson.getId() + "» не найдено.");
        }
        //проверяем существует ли предмет с указанным Id
        if (!subjectRepository.existsById(lesson.getSubject().getId())) {
            validationException.put("id", "Предмет с id «" + lesson.getSubject().getId() + "» не найден.");
        }
        //проверяем существует ли преподаватель с указанным Id
        if (!teacherRepository.existsById(lesson.getTeacher().getId())) {
            validationException.put("id", "Преподаватель с id «" + lesson.getTeacher().getId() + "» не найден.");
        }
        //проверка даты начала занятия
        if (lesson.getStartDateTime() == null) {
            validationException.put("startdatetime", "В занятии с id «" + lesson.getId() + "» не указана дата начала занятия.");
        } else if (lesson.getStartDateTime().toLocalDate().isBefore(LocalDate.now().minusDays(1))) {
            validationException.put("startdatetime", "Дата начала занятия должна быть не позднее, чем день назад.");
        }
        //проверка группы занятий
        if (lesson.getGroup() != null && lesson.getGroup().getId() < 1) {
            validationException.put("group", "Группа занятий должна быть пуста (null) или должна быть больше нуля.");
        }

        validationException.throwExceptionIfIsNotEmpty();
    }
}
