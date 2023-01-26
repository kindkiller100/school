package com.school.school.lessons;

import com.school.school.exceptions.ValidationException;
import com.school.school.students.StudentRepository;
import com.school.school.subjects.SubjectRepository;
import com.school.school.teachers.TeacherRepository;
import com.school.school.utils.DateTimeRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.time.LocalDate;
import java.util.List;

@Service
public class LessonService {
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private SubjectRepository subjectRepository;

    //получить все занятия
    public List<Lesson> list(){
        return lessonRepository.findAll();
    }

    //получить занятие по id
    public Lesson getIfExists(long id){
        return lessonRepository.getIfExists(id);
    }

    //получить все занятия из диапазона дат
    public List<Lesson> getAllInDateRange(DateTimeRange dateRange){
        dateRange.validate();
        return lessonRepository.findLessonsByStartDateTimeBetween(dateRange.getFrom(), dateRange.getTo());
    }

    //получить все занятия по id преподавателя
    public List<Lesson> getAllByTeacherId(long id) {
        return lessonRepository.findLessonsByTeacherId(id);
    }

    //получить все занятия по id студента
    public List<Lesson> getAllByStudentId(long id) {
        return lessonRepository.findLessonsByStudentId(id);
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
        return lessonRepository.countDurationOfLessonsByTeacherInRange(id, dateTimeRange.getFrom(), dateTimeRange.getTo())/60d;
    }

    //количество часов занятий, посещенных студентом за период
    public double countHoursOfLessonsByStudentInRange(long id, DateTimeRange dateTimeRange) {
        ValidationException validationException = new ValidationException();

        //проверяем, что записть с таким Id существует
        if (!studentRepository.existsById(id)) {
            validationException.put("id", "Студент с id «" + id + "» не найден.");
        }
        // проверка диапазона дат
        if (!dateTimeRange.isValid()) {
            validationException.put("date_time_range", DateTimeRange.ERR_STRING);
        }

        validationException.throwExceptionIfIsNotEmpty();

        //получаем количество минут занятий, посещенных студеном за период, и переводим в часы
        return lessonRepository.findDurationByStudentIdInRange(dateTimeRange.getFrom(), dateTimeRange.getTo(), id)/ 60d;
    }

    //создание занятия
    //параметр lesson содержит поля subject и teacher, у которых заполнен только id (после маппинга из LessonDtoIn)
    public void create(Lesson lesson){
        validate(lesson, false);
        lessonRepository.save(lesson);
    }

    //удаление занятия по id
    public void delete(long id){
        if (lessonRepository.existsById(id)) {      //проверяем, есть ли запись с таким id в базе данных
            lessonRepository.deleteById(id);        //удаляем запись по id
        } else {                                    //если записи нет - выбрасываем ошибку
            throw new ValidationException("id", "Занятие с id «" + id + "» не найдено.").setStatus(HttpStatus.NOT_FOUND);
        }
    }

    //редактирование занятия
    //параметр editLesson содержит поля subject и teacher, у которых заполнен только id (после маппинга из LessonDtoIn)
    public void edit(Lesson editLesson){
        validate(editLesson, true);
        //сохраняем запись с измененными данными в БД
        lessonRepository.save(editLesson);
    }

    private void validate(Lesson lesson, boolean editFlag) {
        ValidationException validationException = new ValidationException();

        //проверяем, есть ли запись с таким id в базе данных
        if (editFlag && !lessonRepository.existsById(lesson.getId())) {
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

        validationException.throwExceptionIfIsNotEmpty();
    }
}
