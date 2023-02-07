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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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
        Set<Student> students = lessonsGroupsDtoIn.getStudents()
                .stream()
                .map(id -> studentRepository.getIfExists(id))
                .collect(Collectors.toSet());
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
        //создаем список занятий, которые нужно удалить
        Set<Long> deletedLessons = new HashSet<>();
        //получаем редактируемый объект LessonsGroups из БД, чтобы в нем установить изменения и сохранить этот объект обратно в БД
        LessonsGroups oldLessonsGroups = lessonsGroupsRepository.getIfExists(editLessonsGroupsDtoIn.getLessonsGroupId());
        //проверяем совпадение названия
        if (!oldLessonsGroups.getTitle().equals(editLessonsGroupsDtoIn.getLessonsGroupTitle())) {
            oldLessonsGroups.setTitle(editLessonsGroupsDtoIn.getLessonsGroupTitle());
        }
        //конвертируем строку расписания в список объектов
        List<Schedule> oldSchedules = Schedule.convertToListOfSchedules(oldLessonsGroups.getSchedule());
        //TODO: Порядок следования элементов в коллекции имеет значение. Есть ли смысл сортировать обе коллекции расписаний по дням недели?
        //проверяем совпадение расписаний
        if (!Objects.equals(oldSchedules, editLessonsGroupsDtoIn.getSchedules())) {
            oldLessonsGroups.setSchedule(editLessonsGroupsDtoIn.createStringOfSchedules());
        }
        //создаем объекты предмета и преподавателя, а также список студентов для проверки на эквивалентность
        Subject subject = Subject.builder().setId(editLessonsGroupsDtoIn.getSubjectId()).build();
        Teacher teacher = Teacher.builder().setId(editLessonsGroupsDtoIn.getTeacherId()).build();
        //списку учеников присваиваем null. Список формируется из БД, поэтому формируем только при необходимости один раз.
        //Потенциально может возникнуть случай, при котором нам не понадобится список учеников. Тогда и обращения к БД тоже не будет.
        Set<Student> newStudents = null;

        //если у группы занятий есть связанный с ним список занятий
        if (!oldLessonsGroups.getLessons().isEmpty()) {
            //находим самое последнее занятие, это будет базовое занятие, по которому будем проверять изменения в занятиях
            Lesson baseLesson = oldLessonsGroups.getLessons()
                    .stream()
                    .max(Comparator.comparing(Lesson::getStartDateTime))
                    .get();
            //проверяем совпадение предмета
            if (baseLesson.getSubject().getId() != editLessonsGroupsDtoIn.getSubjectId()) {
                oldLessonsGroups.getLessons()
                        .stream()
                        //отбираем только те занятия, которые лежат в диапазоне редактирования группы
                        .filter(lesson -> lesson.getStartDateTime().isAfter(editLessonsGroupsDtoIn.getDateRange().getFrom()) &&
                                lesson.getStartDateTime().isBefore(editLessonsGroupsDtoIn.getDateRange().getTo()))
                        //устанавливаем предмет
                        .forEach(lesson -> lesson.setSubject(subject));
            }
            //проверяем совпадение преподавателя
            if (baseLesson.getTeacher().getId() != editLessonsGroupsDtoIn.getTeacherId()) {
                oldLessonsGroups.getLessons()
                        .stream()
                        .filter(lesson -> lesson.getStartDateTime().isAfter(editLessonsGroupsDtoIn.getDateRange().getFrom()) &&
                                lesson.getStartDateTime().isBefore(editLessonsGroupsDtoIn.getDateRange().getTo()))
                        .forEach(lesson -> lesson.setTeacher(teacher));
            }
            //проверяем совпадение списков студентов
            if (!Objects.equals(baseLesson.getStudents().stream().map(Student::getId).collect(Collectors.toSet()),
                    editLessonsGroupsDtoIn.getStudents())) {
                //если список студентов равен null, то создаем его
                newStudents = Objects.requireNonNullElseGet(newStudents, () -> editLessonsGroupsDtoIn.getStudents()
                        .stream()
                        .map(id -> studentRepository.getIfExists(id))
                        .collect(Collectors.toSet()));
                Set<Student> finalNewStudents = newStudents;
                oldLessonsGroups.getLessons()
                        .stream()
                        .filter(lesson -> lesson.getStartDateTime().isAfter(editLessonsGroupsDtoIn.getDateRange().getFrom()) &&
                                lesson.getStartDateTime().isBefore(editLessonsGroupsDtoIn.getDateRange().getTo()))
                        .forEach(lesson -> lesson.setStudents(finalNewStudents));
            }
            //проверяем совпадение расписаний
            if (!Objects.equals(oldSchedules, editLessonsGroupsDtoIn.getSchedules())) {
                //создаем список новых расписаний
                List<Schedule> newSchedules = new ArrayList<>(editLessonsGroupsDtoIn.getSchedules());
                //создаем список расписаний, которые есть в обоих списках расписаний: старом (который в БД) и новом (который пришел с UI)
                List<Schedule> schedulesInBothLists = oldSchedules.stream()
                        .filter(newSchedules::contains)
                        .toList();
                //убиаем из обоих списков одинаковые распивания
                if (!schedulesInBothLists.isEmpty()) {
                    newSchedules.removeIf(schedulesInBothLists::contains);
                    oldSchedules.removeIf(schedulesInBothLists::contains);
                }
                //если оба списка расписаний не пусты
                if (!newSchedules.isEmpty() & !oldSchedules.isEmpty()) {
                    //находим минимальное количество элементов в списках
                    byte countOfSchedules = (byte) Math.min(newSchedules.size(), oldSchedules.size());
                    //меняем дату начала и продолжительность занятия по старому расписанию на новые значения по новому расписанию
                    for (int i = 0; i < countOfSchedules; i++) {
                        oldLessonsGroups.getLessons()
                                .stream()
                                .filter(lesson -> lesson.getStartDateTime().isAfter(editLessonsGroupsDtoIn.getDateRange().getFrom()) &&
                                        lesson.getStartDateTime().isBefore(editLessonsGroupsDtoIn.getDateRange().getTo()))
                                .filter(lesson -> lesson.getStartDateTime().getDayOfWeek() == DayOfWeek.of(oldSchedules.get(0).getDayOfWeek()))
                                .forEach(lesson -> {
                                    lesson.setDuration(newSchedules.get(0).getDuration());
                                    //меняем дату занятия: сдвигаем дни и устанавливаем время
                                    lesson.setStartDateTime(LocalDateTime.of(
                                            lesson.getStartDateTime()
                                                    .plusDays(newSchedules.get(0).getDayOfWeek() - oldSchedules.get(0).getDayOfWeek())
                                                    .toLocalDate(),
                                                    LocalTime.parse(newSchedules.get(0).getTimeOfStart(), DateTimeFormatter.ofPattern("HH:mm"))
                                            )
                                    );
                                });
                        //удаляем отработанные расписания
                        newSchedules.remove(0);
                        oldSchedules.remove(0);
                    }
                }
                //после предыдущей операции остались либо новые расписания, которые необходимо добавить, либо старые, которые необходимо удалить
                //если остались старые расписания
                if (!oldSchedules.isEmpty()) {
                    for (Schedule schedule: oldSchedules) {
                        Iterator<Lesson> lessonIterator = oldLessonsGroups.getLessons().iterator();
                        while (lessonIterator.hasNext()) {
                            Lesson lesson = lessonIterator.next();
                            if (lesson.getStartDateTime().isAfter(editLessonsGroupsDtoIn.getDateRange().getFrom()) &&
                                    lesson.getStartDateTime().isBefore(editLessonsGroupsDtoIn.getDateRange().getTo()) &&
                                    lesson.getStartDateTime().getDayOfWeek() == DayOfWeek.of(schedule.getDayOfWeek())) {
                                //добавляем Id занятия в список удаляемых заниятий
                                deletedLessons.add(lesson.getId());
                                //удаляем заниятие из списка занятий, связанных с редактируемой группой занятий
                                lessonIterator.remove();
                            }
                        }
                    }
                }
                //если остались новые расписания, добавляем занятия по этим расписаниям за редактируемый период
                if (!newSchedules.isEmpty()) {
                    newStudents = Objects.requireNonNullElseGet(newStudents, () -> editLessonsGroupsDtoIn.getStudents()
                            .stream()
                            .map(id -> studentRepository.getIfExists(id))
                            .collect(Collectors.toSet()));
                    Set<Lesson> lessons = createSetOfLessons(editLessonsGroupsDtoIn,
                            newSchedules,
                            editLessonsGroupsDtoIn.getDateRange(),
                            oldLessonsGroups,
                            newStudents);
                    oldLessonsGroups.getLessons().addAll(lessons);
                }
            }
            //проверка диапазона дат
            //если дата начала диапазона редактирования группы занятий позже текущей даты,
            // то удаляем все занятия за период от текущей даты до даты начала диапазона минус 1 день
            LocalDateTime dateRangeFrom = LocalDate.now().atTime(LocalTime.MIN);
            LocalDateTime dateRangeTo = editLessonsGroupsDtoIn.getDateRange().getFrom().toLocalDate().minusDays(1).atTime(LocalTime.MAX);
            if (dateRangeTo.isAfter(dateRangeFrom)) {
                Iterator<Lesson> lessonIterator = oldLessonsGroups.getLessons().iterator();
                while (lessonIterator.hasNext()) {
                    Lesson lesson = lessonIterator.next();
                    if (lesson.getStartDateTime().isAfter(dateRangeFrom) &&
                            lesson.getStartDateTime().isBefore(dateRangeTo)) {
                        deletedLessons.add(lesson.getId());
                        lessonIterator.remove();
                    }
                }
            }
            //если дата конца диапазона редактирования группы маньше даты последнего занятия,
            //то нужно удалить все занятия с даты конца редактирования группы плюс день по дату последнего занятия
            dateRangeFrom = editLessonsGroupsDtoIn.getDateRange().getTo().toLocalDate().plusDays(1).atTime(LocalTime.MIN);
            dateRangeTo = baseLesson.getStartDateTime().toLocalDate().plusDays(1).atTime(LocalTime.MAX);
            if (dateRangeTo.isAfter(dateRangeFrom)) {
                Iterator<Lesson> lessonIterator = oldLessonsGroups.getLessons().iterator();
                while (lessonIterator.hasNext()) {
                    Lesson lesson = lessonIterator.next();
                    if (lesson.getStartDateTime().isAfter(dateRangeFrom) &&
                            lesson.getStartDateTime().isBefore(dateRangeTo)) {
                        deletedLessons.add(lesson.getId());
                        lessonIterator.remove();
                    }
                }
            }
            //если дата конца диапазона редактирования группы больше даты начала последнего занятия,
            //то нужно добавить занятия за период от даты начала последнего занятия плюс день по дату конца диапазона редактирования
            dateRangeFrom = baseLesson.getStartDateTime().toLocalDate().plusDays(1).atTime(LocalTime.MIN);
            dateRangeTo = editLessonsGroupsDtoIn.getDateRange().getTo().toLocalDate().atTime(LocalTime.MAX);
            if (dateRangeFrom.isBefore(dateRangeTo)) {
                DateTimeRange dateRange = new DateTimeRange(dateRangeFrom, dateRangeTo);
                newStudents = Objects.requireNonNullElseGet(newStudents, () -> editLessonsGroupsDtoIn.getStudents()
                        .stream()
                        .map(id -> studentRepository.getIfExists(id))
                        .collect(Collectors.toSet()));
                Set<Lesson> lessons = createSetOfLessons(editLessonsGroupsDtoIn,
                        editLessonsGroupsDtoIn.getSchedules(),
                        dateRange,
                        oldLessonsGroups,
                        newStudents);
                oldLessonsGroups.getLessons().addAll(lessons);
            }
            //если дата начала диапазона редактирования группы меньше даты начала первого занятия в группе,
            //то нужно добавить занятия за период от даты начала диапазона редактирования по дату начала первого занятия минус один день
            dateRangeFrom = editLessonsGroupsDtoIn.getDateRange().getFrom().toLocalDate().atTime(LocalTime.MIN);
            dateRangeTo = oldLessonsGroups.getLessons()
                    .stream()
                    .min(Comparator.comparing(Lesson::getStartDateTime))
                    .get()
                    .getStartDateTime()
                    .toLocalDate()
                    .minusDays(1)
                    .atTime(LocalTime.MAX);
            if (dateRangeFrom.isBefore(dateRangeTo)) {
                DateTimeRange dateRange = new DateTimeRange(dateRangeFrom, dateRangeTo);
                newStudents = Objects.requireNonNullElseGet(newStudents, () -> editLessonsGroupsDtoIn.getStudents()
                        .stream()
                        .map(id -> studentRepository.getIfExists(id))
                        .collect(Collectors.toSet()));
                Set<Lesson> lessons = createSetOfLessons(editLessonsGroupsDtoIn,
                        editLessonsGroupsDtoIn.getSchedules(),
                        dateRange,
                        oldLessonsGroups,
                        newStudents);
                oldLessonsGroups.getLessons().addAll(lessons);
            }
        } else {    //иначе (у группы занятий нет списка связанных с ней занятий)
            //добавляем занятия за редактируемый период
            newStudents = Objects.requireNonNullElseGet(newStudents, () -> editLessonsGroupsDtoIn.getStudents()
                    .stream()
                    .map(id -> studentRepository.getIfExists(id))
                    .collect(Collectors.toSet()));
            Set<Lesson> lessons = createSetOfLessons(editLessonsGroupsDtoIn,
                    editLessonsGroupsDtoIn.getSchedules(),
                    editLessonsGroupsDtoIn.getDateRange(),
                    oldLessonsGroups,
                    newStudents);
            oldLessonsGroups.setLessons(lessons);
        }
        //удаляем лишние занятия
        if (!deletedLessons.isEmpty()) {
            lessonRepository.deleteByIdIn(deletedLessons);
        }
        //записываем измененную группу занятий
        lessonsGroupsRepository.save(oldLessonsGroups);
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
                    validationException.put("dayOfWeek:" + (i + 1), "Номер дня недели должен быть от 1 до 7 включительно.");
                }
                if (schedule.getTimeOfStart() == null) {
                    validationException.put("timeOfStart:" + (i + 1), "Время начала занятия не должно быть пустым.");
                } else if (!schedule.getTimeOfStart().matches("([01][0-9]|2[0-3]):[0-5][0-9]")) {
                    validationException.put("timeOfStart:" + (i + 1), "Время начала занятия не соответствует формату чч:мм.");
                }
                if (schedule.getDuration() < 30 || schedule.getDuration() > 210) {
                    validationException.put("duration:" + (i + 1), "Продолжительность занятия должна быть в пределах от 30 до 210 минут.");
                }
            }
        } else {
            validationException.put("schedules", "Не удалось создать объект писка расписаний.");
        }
        validationException.throwExceptionIfIsNotEmpty();
    }
}
