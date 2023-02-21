package com.school.school.lessons;

import com.school.school.exceptions.ValidationException;
import com.school.school.lessons_groups.LessonsGroupRepository;
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
import java.time.LocalDateTime;
import java.util.List;

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
    @Autowired
    private LessonsGroupRepository lessonsGroupRepository;

    //получить все занятия
    public Page<Lesson> list(Pageable pageable) {
        PageableValidator.sortValidOrThrow(Lesson.class, pageable);
        return repository.findAll(pageable);
    }

    //получить занятие по id
    public Lesson getIfExists(long id){
        return repository.getIfExists(id);
    }

    //получить все занятия из диапазона дат
    public Page<Lesson> getAllInDateRange(DateTimeRange dateRange, Pageable pageable){
        ValidationException validationException = new ValidationException();
        validationException.put(PageableValidator.sortValid(Lesson.class, pageable));
        validationException.put(dateRange.validate(false));
        validationException.throwExceptionIfIsNotEmpty();

        return repository.findLessonsByStartDateTimeBetween(dateRange.getFrom(), dateRange.getTo(), pageable);
    }

    //получить все занятия по id преподавателя
    public Page<Lesson> getAllByTeacherId(long id, Pageable pageable) {
        PageableValidator.sortValidOrThrow(Lesson.class, pageable);
        return repository.findLessonsByTeacherId(id, pageable);
    }

    //получить все занятия по id студента
    public Page<Lesson> getAllByStudentId(long id, Pageable pageable) {
        PageableValidator.sortValidOrThrow(Lesson.class, pageable);
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
        validationException.put(dateTimeRange.validate(false));

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
        validationException.put(dateTimeRange.validate(false));

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

    //TODO: add transactional
    public void clearGroupIdBeforeDateTime(long groupId, LocalDateTime dateTime) {
        List<Lesson> lessons = repository.findLessonByGroupIdAndStartDateTimeBefore(groupId, dateTime);

        lessons.forEach(lesson -> repository.save(lesson.clone().setGroup(null).build()));
    }

    public void deleteAllByGroupIdAndStartDateTimeAfter(long groupId, LocalDateTime startDateTime) {
        repository.deleteAllByGroupIdAndStartDateTimeAfter(groupId, startDateTime);
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
        if (lesson.getGroup() != null && !lessonsGroupRepository.existsById(lesson.getGroup().getId())) {
            validationException.put("group", "Группа занятий с id «" + lesson.getGroup().getId() + "» не найден.");
        }

        validationException.throwExceptionIfIsNotEmpty();
    }

    //метод, который осуществляет поиск коллизий расписания по Id преподавателя
    public List<Lesson> findScheduleCollisionsByTeacher(long teacherId) {
        ValidationException validationException = new ValidationException();
        //проверяем, что запись с таким Id существует
        if (!teacherRepository.existsById(teacherId)) {
            validationException.put("id", "Преподпаптель с id «" + teacherId + "» не найден.");
        }
        validationException.throwExceptionIfIsNotEmpty();
        //ищем коллизии в списке занятий
        return findScheduleCollisions(repository.findLessonsByTeacherId(teacherId));
    }

    //метод, который осуществляет поиск коллизий расписания по Id ученика
    public List<Lesson> findScheduleCollisionsByStudent(long studentId) {
        ValidationException validationException = new ValidationException();
        //проверяем, что запись с таким Id существует
        if (!studentRepository.existsById(studentId)) {
            validationException.put("id", "Студент с id «" + studentId + "» не найден.");
        }
        validationException.throwExceptionIfIsNotEmpty();
        //ищем коллизии в списке занятий
        return findScheduleCollisions(repository.findLessonsByStudentId(studentId));
    }

    //метод, который ищет коллизии в списке занятий
    private List<Lesson> findScheduleCollisions(List<Lesson> list) {
        int i = 0;
        //перебираем список занятий
        while (i < list.size()) {
            //текущее занятие преобразуем в диапазон дат
            DateTimeRange baseRange = list.get(i).getDateTimeRange();
            //флаг удаления текущего занятия из списка
            boolean flag = false;
            //перебираем список занятий для сравнения с остальными занятиями
            for (int j = 0; j < list.size(); j++) {
                //не сравниваем с самим собой
                if (i == j) {
                    continue;
                }
                //занятие, с которым сравниваем, преобразуем в диапазон дат
                DateTimeRange rangeForComparison = list.get(j).getDateTimeRange();
                //если диапазоны пересекаются, то флагу присваиваем значение true
                flag |= baseRange.intersection(rangeForComparison);
            }
            //если flag = false (текущее занятие не пересекается с остальными занятиями), то удаляем его
            if (!flag) {
                list.remove(i);
            } else {
                i++;
            }
        }
        return list;
    }
}
