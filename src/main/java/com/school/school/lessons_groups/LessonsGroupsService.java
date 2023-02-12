package com.school.school.lessons_groups;

import com.school.school.exceptions.ValidationException;
import com.school.school.lessons.Lesson;
import com.school.school.lessons.LessonRepository;
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
import java.time.LocalTime;
import java.util.*;

@Service
public class LessonsGroupsService {
    @Autowired
    private LessonsGroupsRepository lessonsGroupsRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private LessonRepository lessonRepository;

    public Page<LessonsGroups> list(Pageable pageable) {
        PageableValidator.checkIsSortValid(LessonsGroups.class, pageable);
        return lessonsGroupsRepository.findAll(pageable);
    }

    public LessonsGroups getIfExists(long id) {
        return lessonsGroupsRepository.getIfExists(id);
    }

    //создание группы занятий
    public void create(LessonsGroupsDtoIn lessonsGroupsDtoIn) {
        //валидация объекта LessonsGroupsDtoIn
        validateLessonsGroupsDtoIn(lessonsGroupsDtoIn, false);
        //создаем объект LessonsGroups, заполняем поля объекта
        LessonsGroups lessonsGroups = LessonsGroups.builder()
                .setId(0)
                .setTitle(lessonsGroupsDtoIn.getLessonsGroupTitle())
                .setSchedule(lessonsGroupsDtoIn.createStringOfSchedules())
                .setLessons(null)
                .build();
        //создаем список студентов
        Set<Student> students = studentRepository.getAllByIdIn(lessonsGroupsDtoIn.getStudents());
        //создаем список занятий
        Set<Lesson> lessons = createSetOfLessons(lessonsGroupsDtoIn,
                lessonsGroupsDtoIn.getSchedules(),
                lessonsGroupsDtoIn.getDateRange(),
                lessonsGroups,
                students);
        //присваиваем список занятий объекту LessonsGroups
        lessonsGroups.setLessons(lessons);
        //записываем объект LessonsGroups в БД
        lessonsGroupsRepository.save(lessonsGroups);
    }

    //редактирование объекта LessonsGroups
    public void edit(LessonsGroupsDtoIn editLessonsGroupsDtoIn) {
        //валидация объекта LessonsGroupsDtoIn
        validateLessonsGroupsDtoIn(editLessonsGroupsDtoIn, true);
        //получаем редактируемый объект LessonsGroups из БД, чтобы в нем установить изменения и сохранить этот объект обратно в БД
        LessonsGroups oldLessonsGroups = lessonsGroupsRepository.getIfExists(editLessonsGroupsDtoIn.getLessonsGroupId());
        //проверяем совпадение названия
        if (!oldLessonsGroups.getTitle().equals(editLessonsGroupsDtoIn.getLessonsGroupTitle())) {
            oldLessonsGroups.setTitle(editLessonsGroupsDtoIn.getLessonsGroupTitle());
        }
        //конвертируем строку расписания в список объектов
        List<Schedule> oldSchedules = Schedule.convertToListOfSchedules(oldLessonsGroups.getSchedule());
        //сортируем старый список расписаний по дням недели
        oldSchedules.sort(Comparator.comparingInt(Schedule::getDayOfWeek));
        //создаем список новых расписаний
        List<Schedule> newSchedules = new ArrayList<>(editLessonsGroupsDtoIn.getSchedules());
        //сортируем новый список расписаний по дням недели
        newSchedules.sort(Comparator.comparingInt(Schedule::getDayOfWeek));
        //проверяем совпадение расписаний
        if (!Objects.equals(oldSchedules, newSchedules)) {
            oldLessonsGroups.setSchedule(editLessonsGroupsDtoIn.createStringOfSchedules());
        }

        //проверяем поля в группе занятий и связанных с ним занятиях
        Boolean checkFieldsLessonGroups = oldLessonsGroups.checkFields(editLessonsGroupsDtoIn);
        if (checkFieldsLessonGroups == null) {
            //создаем объекты предмета и преподавателя, а также список студентов для проверки на эквивалентность
            Subject subject = Subject.builder().setId(editLessonsGroupsDtoIn.getSubjectId()).build();
            Teacher teacher = Teacher.builder().setId(editLessonsGroupsDtoIn.getTeacherId()).build();
            Set<Student> newStudents = studentRepository.getAllByIdIn(editLessonsGroupsDtoIn.getStudents());
            //меняем только поля в занятиях
            oldLessonsGroups.setFieldsIntoLessons(subject, teacher, newStudents, editLessonsGroupsDtoIn.getDateRange());
        } else if (!checkFieldsLessonGroups) {
            //создаем список занятий, которые нужно удалить
            Set<Long> deletedLessons = new HashSet<>();
            //меняем расписания, диапазон занятий и пеля в занятиях
            changeLessons(oldLessonsGroups, editLessonsGroupsDtoIn, deletedLessons, oldSchedules);
            //удаляем лишние занятия
            if (!deletedLessons.isEmpty()) {
                lessonRepository.deleteByIdIn(deletedLessons);
            }
        }
        //записываем измененную группу занятий
        lessonsGroupsRepository.save(oldLessonsGroups);
    }

    private void changeLessons(LessonsGroups lessonsGroups,
                               LessonsGroupsDtoIn lessonsGroupsDtoIn,
                               Set<Long> deletedLessons,
                               List<Schedule> oldSchedules) {
        //создаем объекты предмета и преподавателя, а также список студентов для проверки на эквивалентность
        Subject subject = Subject.builder().setId(lessonsGroupsDtoIn.getSubjectId()).build();
        Teacher teacher = Teacher.builder().setId(lessonsGroupsDtoIn.getTeacherId()).build();
        Set<Student> newStudents = studentRepository.getAllByIdIn(lessonsGroupsDtoIn.getStudents());
        //меняем только поля в занятиях
        lessonsGroups.setFieldsIntoLessons(subject, teacher, newStudents, lessonsGroupsDtoIn.getDateRange());

        //создаем список новых расписаний
        List<Schedule> newSchedules = new ArrayList<>(lessonsGroupsDtoIn.getSchedules());

        //создаем список расписаний, которые есть в обоих списках расписаний: старом (который в БД) и новом (который пришел с UI)
        List<Schedule> schedulesInBothLists = oldSchedules.stream()
                .filter(newSchedules::contains)
                .toList();
        //создаем список дат занятий, которые не нужно менять в рамках изменений расписаний и диапазона дат
        List<LocalDateTime> listOfUnchangedLessons = new ArrayList<>();
        //создаем диапазон дат, в котором не нужно менять занятия
        DateTimeRange dateRange = dateRangeForUnchangedLessons(lessonsGroups.dateRangeOfLessons(), lessonsGroupsDtoIn.getDateRange());
        //заполняем список
        if (!schedulesInBothLists.isEmpty() && dateRange.isValid()) {
            for (Schedule schedule: schedulesInBothLists) {
                listOfUnchangedLessons.addAll(schedule.createListOfDate(dateRange));
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
                    .createListOfDate(lessonsGroupsDtoIn.getDateRange())
                    .stream()
                    .map(date -> new DateAndDuration(date, schedule.getDuration()))
                    .toList());
        }
        //убираем дни, которые не нужно менять
        listOfDaysNewSchedules.removeIf(day -> listOfUnchangedLessons.contains(day.date));
        //нижняя граница даты редактирования занятий (сегодня или начало диапазона редактирования)
        LocalDateTime dateOfRedaction = lessonsGroupsDtoIn.getDateRange().getFrom().isBefore(LocalDateTime.now()) ?
                lessonsGroupsDtoIn.getDateRange().getFrom() : LocalDateTime.now();
        dateOfRedaction = dateOfRedaction.toLocalDate().atTime(LocalTime.MIN);
        //перебираем занятия
        Iterator<Lesson> lessonIterator = lessonsGroups.getLessons().iterator();
        while (lessonIterator.hasNext()) {
            Lesson lesson = lessonIterator.next();
            //отбираем только те занятия, у которых дата начала после нижней границы редактирования занятий
            if (lesson.getStartDateTime().isAfter(dateOfRedaction)) {
                //если даты начала занятия нету в списке дат занятий, которые не нужно менять
                if (!listOfUnchangedLessons.contains(lesson.getStartDateTime())) {
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
                        .setGroup(lessonsGroups)
                        .setStudents(newStudents)
                        .build();
                //добавляем в список группы
                lessonsGroups.getLessons().add(lesson);
            }
        }
    }

    //метод возращает диапазон дат для занятий, которые не должны редактироваться
    //начало диапазона: большее из даты начала диапазона редактирования группы и даты первого занятия в группе
    //конец диапазона: меньшее из даты конца диапазона редактирования группы и даты последнего занятия
    private DateTimeRange dateRangeForUnchangedLessons(DateTimeRange dateRangeOfGroup, DateTimeRange dateRangeDto) {
        if (dateRangeOfGroup == null) {
            return dateRangeDto;
        } else {
            return new DateTimeRange(dateRangeOfGroup.getFrom().isAfter(dateRangeDto.getFrom()) ? dateRangeOfGroup.getFrom() : dateRangeDto.getFrom(),
                    dateRangeOfGroup.getTo().isBefore(dateRangeDto.getTo()) ? dateRangeOfGroup.getTo() : dateRangeDto.getTo());
        }
    }

    //метод для создания списка зантяий
    private Set<Lesson> createSetOfLessons(LessonsGroupsDtoIn lessonsGroupsDtoIn,
                                          List<Schedule> schedules,
                                          DateTimeRange dateRange,
                                          LessonsGroups lessonsGroups,
                                           Set<Student> students) {
        //возвращаемый список занятий
        Set<Lesson> lessons = new HashSet<>();
        //объекты предмета и преподавателя
        Subject subject = Subject.builder().setId(lessonsGroupsDtoIn.getSubjectId()).build();
        Teacher teacher = Teacher.builder().setId(lessonsGroupsDtoIn.getTeacherId()).build();
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
        if (lessonsGroupsRepository.existsById(id)) {
            lessonsGroupsRepository.deleteById(id);
        } else {
            throw new ValidationException("id", "Группа занятий с id «" + id + "» не найдена.")
                    .setStatus(HttpStatus.NOT_FOUND);
        }
    }

    private void validateLessonsGroupsDtoIn(LessonsGroupsDtoIn lessonsGroupsDtoIn, boolean editFlag) {
        ValidationException validationException = new ValidationException();
        //TODO: если объекты = null тоже ругаться. Без них не создать всех записей.
        if (editFlag) {
            if (!lessonsGroupsRepository.existsById(lessonsGroupsDtoIn.getLessonsGroupId())) {
                validationException.put("lessonsGroupId", "Группа занятий с id «" + lessonsGroupsDtoIn.getLessonsGroupId() + "» не найдена.");
            }
            if (lessonsGroupsRepository.existsByTitleAndIdNot(lessonsGroupsDtoIn.getLessonsGroupTitle(), lessonsGroupsDtoIn.getLessonsGroupId())) {
                validationException.put("lessonsGroupTitle", "Группа занятий с именем «" + lessonsGroupsDtoIn.getLessonsGroupTitle() + "» уже существует.");
            }
        } else {
            if (lessonsGroupsRepository.existsByTitle(lessonsGroupsDtoIn.getLessonsGroupTitle())) {
                validationException.put("lessonsGroupTitle", "Группа занятий с именем «" + lessonsGroupsDtoIn.getLessonsGroupTitle() + "» уже существует.");
            }
        }
        if (!subjectRepository.existsById(lessonsGroupsDtoIn.getSubjectId())) {
            validationException.put("subjectId", "Предмет с id «" + lessonsGroupsDtoIn.getSubjectId() + "» не найден.");
        }
        if (!teacherRepository.existsById(lessonsGroupsDtoIn.getTeacherId())) {
            validationException.put("teacherId", "Преподаватель с id «" + lessonsGroupsDtoIn.getTeacherId() + "» не найден.");
        }
        if (lessonsGroupsDtoIn.getStudents() != null) {
            HashSet<Long> students = (HashSet<Long>) lessonsGroupsDtoIn.getStudents();
            for (long studentId: students) {
                if (!studentRepository.existsById(studentId)) {
                    validationException.put("studentId", "Ученик с id «" + studentId + "» не найден.");
                }
            }
        } else {
            validationException.put("students", "Не удалось создать объект списка учеников.");
        }
        if (lessonsGroupsDtoIn.getDateRange() != null) {
            if (lessonsGroupsDtoIn.getDateRange().getFrom() == null) {
                validationException.put("dateRange.from", "Начало диапазона дат не должно быть пустым.");
            } else if (lessonsGroupsDtoIn.getDateRange().getFrom().toLocalDate().isBefore(LocalDate.now().minusDays(1))) {
                validationException.put("dateRange.from", "Дата начала занятий должна быть не позднее, чем день назад.");
            }
            if (lessonsGroupsDtoIn.getDateRange().getTo() == null) {
                validationException.put("dateRange.to", "Конец диапазона дат не должно быть пустым.");
            }
            if (!lessonsGroupsDtoIn.getDateRange().isValid()) {
                validationException.put("dateRange", DateTimeRange.ERR_STRING);
            }
        } else {
            validationException.put("dateRange", "Не удалось создать объект диапазона дат.");
        }
        //TODO: task 87 перенести валидацию в класс Schedule
        if (lessonsGroupsDtoIn.getSchedules() != null) {
            ArrayList<Schedule> schedules = (ArrayList<Schedule>) lessonsGroupsDtoIn.getSchedules();
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
            validationException.put("schedules", "Не удалось создать объект писка расписаний.");
        }
        validationException.throwExceptionIfIsNotEmpty();
    }
}
