package com.school.school.lessons;

import com.school.school.students.StudentRepository;
import com.school.school.teachers.TeacherRepository;
import com.school.school.utils.DateTimeRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;

@Service
public class LessonService {
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TeacherRepository teacherRepository;

    //текст сообщения об ошибке
    private static StringBuilder stringError = new StringBuilder();

    //получить все занятия
    public List<Lesson> list(){
        return lessonRepository.findAll();
    }

    //получить занятие по id
    public Lesson getIfExists(long id){
        return lessonRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Lesson with id «" + id + "» not found."));
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
        stringError.setLength(0);
        //проверяем, что записть с таким Id существует
        if (!teacherRepository.existsById(id)) {
            stringError.append("Teacher with id «" + id + "» not found.");
        }
        // проверка диапазона дат
        if (!dateTimeRange.isValid()) {
            stringError.append(DateTimeRange.ERR_STRING);
        }
        //выводим сообщение об ошибке
        if (!stringError.isEmpty()) {
            throw new NotFoundException(stringError.toString());
        }
        //получаем количество минут занятий, проведенных преподавателем за период, и переводим в часы
        return lessonRepository.countDurationOfLessonsByTeacherInRange(id, dateTimeRange.getFrom(), dateTimeRange.getTo())/60d;
    }

    //количество часов занятий, посещенных студентом за период
    public double countHoursOfLessonsByStudentInRange(long id, DateTimeRange dateTimeRange) {
        stringError.setLength(0);
        //проверяем, что записть с таким Id существует
        if (!studentRepository.existsById(id)) {
            stringError.append("Student with id «" + id + "» not found.");
        }
        // проверка диапазона дат
        if (!dateTimeRange.isValid()) {
            stringError.append(DateTimeRange.ERR_STRING);
        }
        //выводим сообщение об ошибке
        if (!stringError.isEmpty()) {
            throw new NotFoundException(stringError.toString());
        }
        //получаем количество минут занятий, посещенных студеном за период, и переводим в часы
        return lessonRepository.findDurationByStudentIdInRange(dateTimeRange.getFrom(), dateTimeRange.getTo(), id)/ 60d;
    }

    //создание занятия
    public void create(Lesson lesson){
        lesson.startDateValidation();   //проверка даты начала занятия
        lessonRepository.save(lesson);
    }

    //удаление занятия по id
    public void delete(long id){
        if (lessonRepository.existsById(id)) {      //проверяем, есть ли запись с таким id в базе данных
            lessonRepository.deleteById(id);        //удаляем запись по id
        } else {                                    //если записи нет - выбрасываем ошибку
            throw new NotFoundException("Lesson with id «" + id + "» not found.");
        }
    }

    //редактирование занятия
    public void edit(Lesson editLesson){
        if (lessonRepository.existsById(editLesson.getId())){   //проверяем, есть ли запись с таким id в базе данных
            editLesson.startDateValidation();                   //проверка даты начала занятия
            lessonRepository.save(editLesson);                  //сохраняем запись с измененными данными в БД
        } else {                                                //иначе выбрасываем ошибку
            //TODO: add custom exception
            throw new NotFoundException("Lesson with id «" + editLesson.getId() + "» not found.");
        }
    }
}
