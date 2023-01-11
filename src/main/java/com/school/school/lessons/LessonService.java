package com.school.school.lessons;

import com.school.school.students.StudentRepository;
import com.school.school.utils.DateTimeRange;
import com.school.school.utils.LessonsCalculationsDtoIn;
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

    //текст сообщения об ошибке
    private static StringBuilder stringError = new StringBuilder();

    //получить все занятия
    public List<Lesson> list(){
        return lessonRepository.findAll();
    }

    //получить занятие по id
    public Lesson getById(long id){
        return lessonRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Lesson with id «" + id + "» not found."));
    }

    //получить все занятия из диапазона дат
    public List<Lesson> getAllInDateRange(DateTimeRange dateRange){
        stringError.setLength(0);
        dateRange.rangeValidation(stringError);
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
    public double countHoursOfLessonsByTeacherInRange(LessonsCalculationsDtoIn lessonsCalc) {
        stringError.setLength(0);
        //проверяем, что записть с таким Id существует
        if (!lessonRepository.existsById(lessonsCalc.getId())) {
            stringError.append("Lesson with id «" + lessonsCalc.getId() + "» not found.");
        }
        // проверка диапазона дат
        lessonsCalc.getDataTimeRange().rangeValidation(stringError);
        return lessonRepository.countHoursOfLessonsByTeacherInRange(lessonsCalc.getId(), lessonsCalc.getDataTimeRange().getFrom(), lessonsCalc.getDataTimeRange().getTo())/60d;
    }

    //количество часов занятий, посещенных студентом за период
    public double countHoursOfLessonsByStudentInRange(LessonsCalculationsDtoIn lessonsCalc) {
        stringError.setLength(0);
        //проверяем, что записть с таким Id существует
        if (!studentRepository.existsById(lessonsCalc.getId())) {
            stringError.append("Student with id «" + lessonsCalc.getId() + "» not found.");
        }
        // проверка диапазона дат
        lessonsCalc.getDataTimeRange().rangeValidation(stringError);
        return lessonRepository.findDurationByStudentIdInRange(lessonsCalc.getId(), lessonsCalc.getDataTimeRange().getFrom(), lessonsCalc.getDataTimeRange().getTo())/ 60d;
    }

    //создание занятия
    public void create(Lesson lesson){
        //TODO: add validations
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
    public void editById(Lesson editLesson){
        if (lessonRepository.existsById(editLesson.getId())){   //проверяем, есть ли запись с таким id в базе данных
            //TODO: add validations for id, subjectId and teacherId
            lessonRepository.save(editLesson);                  //сохраняем запись с измененными данными в БД
        } else {                                                //иначе выбрасываем ошибку
            //TODO: add custom exception
            throw new NotFoundException("Lesson with id «" + editLesson.getId() + "» not found.");
        }
    }
}
