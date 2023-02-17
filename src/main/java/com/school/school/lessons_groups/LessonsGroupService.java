package com.school.school.lessons_groups;

import com.school.school.exceptions.ValidationException;
import com.school.school.lessons.Lesson;
import com.school.school.lessons.LessonRepository;
import com.school.school.lessons.LessonService;
import com.school.school.students.Student;
import com.school.school.students.StudentRepository;
import com.school.school.subjects.Subject;
import com.school.school.subjects.SubjectRepository;
import com.school.school.teachers.Teacher;
import com.school.school.teachers.TeacherRepository;
import com.school.school.utils.DateTimeRange;
import com.school.school.utils.PageableValidator;
import com.school.school.utils.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class LessonsGroupService {
    @Autowired
    private LessonsGroupRepository repository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private LessonService lessonService;

    public Page<LessonsGroup> list(Pageable pageable) {
        PageableValidator.checkIsSortValid(LessonsGroup.class, pageable);
        return repository.findAll(pageable);
    }

    public LessonsGroup getIfExists(long id) {
        return repository.getIfExists(id);
    }

    //создание группы занятий
    public void create(LessonsGroupDtoIn lessonsGroupDtoIn) {
        //валидация объекта LessonsGroupsDtoIn
        validateLessonsGroupsDtoIn(lessonsGroupDtoIn, false);
        //создаем объект LessonsGroups, заполняем поля объекта
        LessonsGroup lessonsGroup = LessonsGroup.builder()
                .setId(0)
                .setTitle(lessonsGroupDtoIn.getLessonsGroupTitle())
                .setSchedule(Schedule.createStringOfSchedules(lessonsGroupDtoIn.getSchedules()))
                .setLessons(null)
                .build();
        //создаем список студентов
        Set<Student> students = studentRepository.getAllByIdIn(lessonsGroupDtoIn.getStudents());
        //создаем список занятий
        Set<Lesson> lessons = createSetOfLessons(lessonsGroupDtoIn,
                lessonsGroupDtoIn.getSchedules(),
                lessonsGroupDtoIn.getDateRange(),
                lessonsGroup,
                students);
        //присваиваем список занятий объекту LessonsGroups
        lessonsGroup.setLessons(lessons);
        //записываем объект LessonsGroups в БД
        repository.save(lessonsGroup);
    }

    //редактирование объекта LessonsGroups
    public void edit(LessonsGroupDtoIn editLessonsGroupDtoIn) {
        //валидация объекта LessonsGroupsDtoIn
        validateLessonsGroupsDtoIn(editLessonsGroupDtoIn, true);
        //получаем редактируемый объект LessonsGroups из БД, чтобы в нем установить изменения и сохранить этот объект обратно в БД
        LessonsGroup oldLessonsGroup = repository.getIfExists(editLessonsGroupDtoIn.getLessonsGroupId());

        //проверяем поля в группе занятий и связанных с ним занятиях
        Boolean checkFieldsLessonGroups = oldLessonsGroup.checkFields(editLessonsGroupDtoIn);
        if (checkFieldsLessonGroups != true) {
            //создаем объекты предмета и преподавателя, а также список студентов для проверки на эквивалентность
        Subject subject = Subject.builder().setId(editLessonsGroupDtoIn.getSubjectId()).build();
        Teacher teacher = Teacher.builder().setId(editLessonsGroupDtoIn.getTeacherId()).build();
        Set<Student> newStudents = studentRepository.getAllByIdIn(editLessonsGroupDtoIn.getStudents());
        //меняем только поля в занятиях
        oldLessonsGroup.setFieldsIntoLessons(subject, teacher, newStudents, editLessonsGroupDtoIn.getDateRange());
            if (!checkFieldsLessonGroups) {
                //меняем расписания, диапазон занятий и пеля в занятиях
                Set<Long> deletedLessons = changeLessons(oldLessonsGroup,
                        editLessonsGroupDtoIn,
                        subject,
                        teacher,
                        newStudents);
                //удаляем лишние занятия
                if (!deletedLessons.isEmpty()) {
                    lessonRepository.deleteByIdIn(deletedLessons);
                }
            }
        }

        //меняем название и список расписаний в группе если нужно
        oldLessonsGroup.setIfNotEquals(editLessonsGroupDtoIn.getLessonsGroupTitle(), editLessonsGroupDtoIn.getSchedules());
        //записываем измененную группу занятий
        repository.save(oldLessonsGroup);
    }

    private Set<Long> changeLessons(LessonsGroup oldGroup,
                                    LessonsGroupDtoIn newGroup,
                                    Subject subject,
                                    Teacher teacher,
                                    Set<Student> newStudents) {
        //создаем список новых расписаний
        List<Schedule> newSchedules = newGroup.getSchedules();
        //конвертируем строку расписания в список объектов
        List<Schedule> oldSchedules = Schedule.convertToListOfSchedules(oldGroup.getSchedule());

        //создаем список расписаний, которые есть в обоих списках расписаний: старом (который в БД) и новом (который пришел с UI)
        List<Schedule> schedulesInBothLists = oldSchedules.stream()
                .filter(newSchedules::contains)
                .toList();
        //создаем список дат занятий, которые не нужно менять в рамках изменений расписаний и диапазона дат
        List<LocalDateTime> listOfUnchangedLessonsDates = new ArrayList<>();
        //создаем диапазон дат, в котором не нужно менять занятия
        DateTimeRange dateRange = dateRangeForUnchangedLessons(oldGroup.dateRangeOfLessons(), newGroup.getDateRange());
        //заполняем список
        if (!schedulesInBothLists.isEmpty() && dateRange.isValid()) {
            for (Schedule schedule: schedulesInBothLists) {
                listOfUnchangedLessonsDates.addAll(schedule.createListOfDate(dateRange));
            }
        }
        //создаем локальный класс для хранения данных: дата начала занятия и продолжительность занятия
        class DateAndDuration {
            LocalDateTime date;
            short duration;

            DateAndDuration(LocalDateTime date, short duration) {
                this.date = date;
                this.duration = duration;
            }
        }
        //создаем список новых дней, которые нужно добавить или на которые нужно изменить занятия
        List<DateAndDuration> listOfDaysNewSchedules = new ArrayList<>();
        //заполняем этот список
        for (Schedule schedule : newSchedules) {
            listOfDaysNewSchedules.addAll(schedule
                    .createListOfDate(newGroup.getDateRange())
                    .stream()
                    .map(date -> new DateAndDuration(date, schedule.getDuration()))
                    .toList());
        }
        //убираем дни, которые не нужно менять
        listOfDaysNewSchedules.removeIf(day -> listOfUnchangedLessonsDates.contains(day.date));
        //нижняя граница даты редактирования занятий (сегодня или начало диапазона редактирования)
        LocalDateTime dateOfRedaction = DateTimeRange.min(newGroup.getDateRange().getFrom(), LocalDateTime.now());
        //создаем список занятий, которые нужно удалить
        Set<Long> deletedLessons = new HashSet<>();
        //перебираем занятия
        Iterator<Lesson> lessonIterator = oldGroup.getLessons().iterator();
        while (lessonIterator.hasNext()) {
            Lesson lesson = lessonIterator.next();
            //отбираем только те занятия, у которых дата начала после нижней границы редактирования занятий
            if (lesson.getStartDateTime().isAfter(dateOfRedaction)) {
                //если даты начала занятия нету в списке дат занятий, которые не нужно менять
                if (!listOfUnchangedLessonsDates.contains(lesson.getStartDateTime())) {
                    //если список новых дней непустой
                    if (!listOfDaysNewSchedules.isEmpty()) {
                        //меняем дату начала и продолжительность занятия на новые значения
                        lesson.setStartDateTime(listOfDaysNewSchedules.get(0).date);
                        lesson.setDuration(listOfDaysNewSchedules.get(0).duration);
                        lesson.setIfNotEquals(subject, teacher, newStudents);
                        //удаляем отработанный объект из списка
                        listOfDaysNewSchedules.remove(0);
                    } else {
                        //если список новых дат уже пустой, а занятия еще есть, то эти занятия нужно удалить
                        deletedLessons.add(lesson.getId());
                        lessonIterator.remove();
                    }
                }
            }
        }
        //если после замен остались даты занятий, которые нужно добавить, то создаем занятия и добавляем в список
        if (!listOfDaysNewSchedules.isEmpty()) {
            for (DateAndDuration dateAndDuration: listOfDaysNewSchedules) {
                //создаем занятие
                Lesson lesson = Lesson.builder()
                        .setId(0)
                        .setStartDateTime(dateAndDuration.date)
                        .setDuration(dateAndDuration.duration)
                        .setSubject(subject)
                        .setTeacher(teacher)
                        .setGroup(oldGroup)
                        .setStudents(newStudents)
                        .build();
                //добавляем в список группы
                oldGroup.getLessons().add(lesson);
            }
        }
        //возвращаем список id занятий, которые нужно удалить
        return deletedLessons;
    }

    //метод возращает диапазон дат для занятий, которые не должны редактироваться
    //начало диапазона: большее из даты начала диапазона редактирования группы и даты первого занятия в группе
    //конец диапазона: меньшее из даты конца диапазона редактирования группы и даты последнего занятия
    private DateTimeRange dateRangeForUnchangedLessons(DateTimeRange oldDateRange, DateTimeRange newDateRange) {
        if (oldDateRange == null) {
            return newDateRange;
        } else {
            return new DateTimeRange(DateTimeRange.max(oldDateRange.getFrom(), newDateRange.getFrom()),
                    DateTimeRange.min(oldDateRange.getTo(), newDateRange.getTo()));
        }
    }

    //метод для создания списка зантяий
    private Set<Lesson> createSetOfLessons(LessonsGroupDtoIn lessonsGroupDtoIn,
                                           List<Schedule> schedules,
                                           DateTimeRange dateRange,
                                           LessonsGroup lessonsGroups,
                                           Set<Student> students) {
        //возвращаемый список занятий
        Set<Lesson> lessons = new HashSet<>();
        //объекты предмета и преподавателя
        Subject subject = Subject.builder().setId(lessonsGroupDtoIn.getSubjectId()).build();
        Teacher teacher = Teacher.builder().setId(lessonsGroupDtoIn.getTeacherId()).build();
        //цикл по расписаниям
        for (Schedule schedule: schedules) {
            //создаем список дат для расписания
            List<LocalDateTime> listOfDates = schedule.createListOfDate(dateRange);
            //цикл по датам
            for (LocalDateTime date: listOfDates) {
                //создаем занятие
                Lesson lesson = Lesson.builder()
                        .setId(0)
                        .setStartDateTime(date)
                        .setDuration(schedule.getDuration())
                        .setSubject(subject)
                        .setTeacher(teacher)
                        .setGroup(lessonsGroups)
                        .setStudents(students)
                        .build();
                //добавляем в список
                lessons.add(lesson);
            }
        }
        return lessons;
    }

    public void delete(long id) {
        if (repository.existsById(id)) {
            LocalDateTime startDateTime = LocalDateTime.now();

            //у занятий, которые закончились на данный момент времени или идут сейчас, устанавливаем groupId = null
            lessonService.clearGroupIdBeforeDateTime(id, startDateTime.plusSeconds(1));

            //остальные занятия, которых еще не было и они не успели начаться, удаляем
            lessonService.deleteAllByGroupIdAndStartDateTimeAfter(id, startDateTime);

            repository.deleteById(id);
        } else {
            throw new ValidationException("id", "Группа занятий с id «" + id + "» не найдена.")
                    .setStatus(HttpStatus.NOT_FOUND);
        }
    }

    private void validateLessonsGroupsDtoIn(LessonsGroupDtoIn lessonsGroupDtoIn, boolean editFlag) {
        ValidationException validationException = new ValidationException();
        if (editFlag) {
            if (!repository.existsById(lessonsGroupDtoIn.getLessonsGroupId())) {
                validationException.put("lessonsGroupId", "Группа занятий с id «" + lessonsGroupDtoIn.getLessonsGroupId() + "» не найдена.");
            }
            if (repository.existsByTitleAndIdNot(lessonsGroupDtoIn.getLessonsGroupTitle(), lessonsGroupDtoIn.getLessonsGroupId())) {
                validationException.put("lessonsGroupTitle", "Группа занятий с именем «" + lessonsGroupDtoIn.getLessonsGroupTitle() + "» уже существует.");
            }
        } else {
            if (repository.existsByTitle(lessonsGroupDtoIn.getLessonsGroupTitle())) {
                validationException.put("lessonsGroupTitle", "Группа занятий с именем «" + lessonsGroupDtoIn.getLessonsGroupTitle() + "» уже существует.");
            }
        }
        if (!subjectRepository.existsById(lessonsGroupDtoIn.getSubjectId())) {
            validationException.put("subjectId", "Предмет с id «" + lessonsGroupDtoIn.getSubjectId() + "» не найден.");
        }
        if (!teacherRepository.existsById(lessonsGroupDtoIn.getTeacherId())) {
            validationException.put("teacherId", "Преподаватель с id «" + lessonsGroupDtoIn.getTeacherId() + "» не найден.");
        }
        if (lessonsGroupDtoIn.getStudents() != null) {
            HashSet<Long> students = (HashSet<Long>) lessonsGroupDtoIn.getStudents();
            for (long studentId: students) {
                if (!studentRepository.existsById(studentId)) {
                    validationException.put("studentId", "Ученик с id «" + studentId + "» не найден.");
                }
            }
        } else {
            validationException.put("students", "Не удалось создать объект списка учеников.");
        }
        if (lessonsGroupDtoIn.getDateRange() != null) {
            if (lessonsGroupDtoIn.getDateRange().getFrom() == null) {
                validationException.put("dateRange.from", "Начало диапазона дат не должно быть пустым.");
            } else if (lessonsGroupDtoIn.getDateRange().getFrom().toLocalDate().isBefore(LocalDate.now().minusDays(1))) {
                validationException.put("dateRange.from", "Дата начала занятий должна быть не позднее, чем день назад.");
            }
            if (lessonsGroupDtoIn.getDateRange().getTo() == null) {
                validationException.put("dateRange.to", "Конец диапазона дат не должно быть пустым.");
            }
            if (!lessonsGroupDtoIn.getDateRange().isValid()) {
                validationException.put("dateRange", DateTimeRange.ERR_STRING);
            }
        } else {
            validationException.put("dateRange", "Не удалось создать объект диапазона дат.");
        }
        //TODO: task 87 перенести валидацию в класс Schedule
        if (lessonsGroupDtoIn.getSchedules() != null && !lessonsGroupDtoIn.getSchedules().isEmpty()) {
            ArrayList<Schedule> schedules = (ArrayList<Schedule>) lessonsGroupDtoIn.getSchedules();
            for (int i = 0; i < schedules.size(); i++) {
                Schedule schedule = schedules.get(i);
                if (schedule.getDayOfWeek() < 1 || schedule.getDayOfWeek() > 7) {
                    validationException.put("dayOfWeek:" + i, "Номер дня недели должен быть от 1 до 7 включительно.");
                }
                if (schedule.getTimeOfStart() == null) {
                    validationException.put("timeOfStart:" + i, "Время начала занятия не должно быть пустым.");
                } else if (!schedule.getTimeOfStart().matches("([01][0-9]|2[0-3]):[0-5][0-9]")) {
                    validationException.put("timeOfStart:" + i, "Время начала занятия не соответствует формату чч:мм.");
                }
                if (schedule.getDuration() < 30 || schedule.getDuration() > 210) {
                    validationException.put("duration:" + i, "Продолжительность занятия должна быть в пределах от 30 до 210 минут.");
                }
            }
        } else {
            validationException.put("schedules", "Не удалось создать объект списка расписаний.");
        }
        validationException.throwExceptionIfIsNotEmpty();
    }
}
