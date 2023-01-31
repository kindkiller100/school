package com.school.school.lessons_groups;

import com.school.school.exceptions.ValidationException;
import com.school.school.lessons.Lesson;
import com.school.school.lessons.LessonRepository;
import com.school.school.students.StudentRepository;
import com.school.school.subjects.Subject;
import com.school.school.subjects.SubjectRepository;
import com.school.school.teachers.Teacher;
import com.school.school.teachers.TeacherRepository;
import com.school.school.utils.DateTimeRange;
import com.school.school.utils.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/lessons_groups")
public class LessonsGroupsController {
    @Autowired
    private LessonsGroupsService service;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private LessonsGroupsRepository lessonsGroupsRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private LessonRepository lessonRepository;

    @GetMapping
    public Page<LessonsGroups> list(Pageable pageable) {
        return service.list(pageable);
    }

    @GetMapping("/{id}")
    public LessonsGroups getById(@PathVariable long id) {
        return service.getIfExists(id);
    }

    @PostMapping
    public void create(@Valid @RequestBody LessonsGroupsDtoIn lessonsGroupsDtoIn) {
        validateLessonsGroupsDtoIn(lessonsGroupsDtoIn, false);
        LessonsGroups lessonsGroups = LessonsGroups.builder()
                .setId(0)
                .setTitle(lessonsGroupsDtoIn.getLessonsGroupTitle())
                .setSchedule(lessonsGroupsDtoIn.createStringOfSchedules())
                .setLessons(null)
                .build();
        Set<Lesson> lessons = new HashSet<>();
        List<Schedule> listOfSchedules = lessonsGroupsDtoIn.getSchedules();
        for (Schedule schedule: listOfSchedules) {
            List<LocalDateTime> listOfDates = schedule.createListOfDate(lessonsGroupsDtoIn.getDateRange());
            for (LocalDateTime date: listOfDates) {
                Lesson lesson = Lesson.builder()
                        .setId(0)
                        .setStartDateTime(date)
                        .setDuration(schedule.getDuration())
                        .setSubject(Subject.builder().setId(lessonsGroupsDtoIn.getSubjectId()).build())
                        .setTeacher(Teacher.builder().setId(lessonsGroupsDtoIn.getTeacherId()).build())
                        .setGroup(lessonsGroups)
                        .build();
                lessons.add(lesson);
            }
        }
        lessonsGroups.setLessons(lessons);
        service.create(lessonsGroups);
    }

    @PutMapping
    public void edit(@Valid @RequestBody LessonsGroups lessonsGroups) {
        service.edit(lessonsGroups);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        service.delete(id);
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
            if ((lessonsGroupsDtoIn.getDateRange().getFrom() != null & lessonsGroupsDtoIn.getDateRange().getTo() != null)
                    && !lessonsGroupsDtoIn.getDateRange().isValid()) {
                validationException.put("dateRange", DateTimeRange.ERR_STRING);
            }
        }
        //TODO: task 87 перенести валидацию в
        if (lessonsGroupsDtoIn.getSchedules() != null) {
            ArrayList<Schedule> schedules = (ArrayList<Schedule>) lessonsGroupsDtoIn.getSchedules();
            for (int i = 0; i < schedules.size(); i++) {
                Schedule schedule = schedules.get(i);
                if (schedule.getDayOfWeek() < 1 || schedule.getDayOfWeek() > 7) {
                    validationException.put("dayOfWeek", "День " + i + ". Номер дня недели должен быть от 1 до 7 включительно.");
                }
                if (schedule.getTimeOfStart() == null) {
                    validationException.put("timeOfStart", "День " + i + ". Время начала занятия не должно быть пустым.");
                } else if (!schedule.getTimeOfStart().matches("([01][0-9]|2[0-3]):[0-5][0-9]")) {
                    validationException.put("timeOfStart", "День " + i + ". Время начала занятия не соответствует формату чч:мм.");
                }
                if (schedule.getDuration() < 30 || schedule.getDuration() > 210) {
                    validationException.put("duration", "День " + i + ". Продолжительность занятия должна быть в пределах от 30 до 210 минут.");
                }
            }
        }
        validationException.throwExceptionIfIsNotEmpty();
    }
}
