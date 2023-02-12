package com.school.school.lessons_groups;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.school.school.lessons.Lesson;
import com.school.school.students.Student;
import com.school.school.subjects.Subject;
import com.school.school.teachers.Teacher;
import com.school.school.utils.CorrectSchedule;
import com.school.school.utils.DateTimeRange;
import com.school.school.utils.Schedule;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "lessons_groups", schema="school_db")
public class LessonsGroups {
    //id группы занятий
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    //название группы занятий
    @NotBlank(message = "Не должно быть пустым.")
    @Size(min = 5, message = "Длина названия группы занятий должна быть не менее 5 символов.")
    private String title;
    //расписание. Формат: 'день_недели,время_начала_занятия,продолжительность_занятия_в_минутах;...'
    @CorrectSchedule(message = "Расписание не соответствует нужному формату.")
    private String schedule;
    //список занятий
    @JsonIgnore
    @OneToMany(mappedBy = "group", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Lesson> lessons = new HashSet<>();

    //конструктор без параметров
    protected LessonsGroups() {
    }

    //конструктор со всеми параметрами
    private LessonsGroups(long id, String title, String schedule, Set<Lesson> lessons) {
        this.id = id;
        this.title = title;
        this.schedule = schedule;
        this.lessons = lessons;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSchedule() {
        return schedule;
    }

    public Set<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(Set<Lesson> lessons) {
        this.lessons = lessons;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    //переопределение метода equals()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LessonsGroups that = (LessonsGroups) o;
        return id == that.id;
    }

    //переопределение метода hashCode()
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    //переопределение метода toString()
    @Override
    public String toString() {
        return "LessonsGroups{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", schedule='" + schedule + '\'' +
                '}';
    }

    //метод, который проверяет соответствие полей текущей группы и объекта LessonsGroupsDtoIn
    //проверяемые поля: диапазон дат занятий, список расписаний, предмет, преподаватель и список студентов
    //возвращаемое значение: true - все поля совпадают
    //                       false - не совпадают списки расписаний и диапазоны дат
    //                       null - не совпадают только поля объектов Lesson
    public Boolean checkFields(LessonsGroupsDtoIn lessonsGroupsDtoIn) {
        //создаем диапазон дат по занятиям группы
        DateTimeRange dateTimeRange = dateRangeOfLessons();
        if (!Objects.equals(dateTimeRange, lessonsGroupsDtoIn.getDateRange())) {
            return false;
        }
        //конвертируем строку расписания в список объектов
        List<Schedule> oldSchedules = Schedule.convertToListOfSchedules(this.schedule);
        if (oldSchedules != null) {
            //сортируем старый список расписаний по дням недели
            oldSchedules.sort(Comparator.comparingInt(Schedule::getDayOfWeek));
        }
        //сравниваем старый и новый списки расписаний
        //(если прошла предыдущая проверка, значит есть список занятий, который нужно актуализировать)
        if (!Objects.equals(oldSchedules, lessonsGroupsDtoIn
                .getSchedules()
                .stream()
                .sorted(Comparator.comparingInt(Schedule::getDayOfWeek))
                .toList())) {
            return false;
        }
        //находим самое последнее занятие, это будет базовое занятие, по которому будем проверять изменения в занятиях
        Lesson baseLesson = lessons
                .stream()
                .max(Comparator.comparing(Lesson::getStartDateTime))
                .orElse(null);
        if (baseLesson != null) {
            //проверяем совпадение предмета, преподавателя и списка студентов
            return baseLesson.checkEqualsFields(lessonsGroupsDtoIn.getSubjectId(),
                    lessonsGroupsDtoIn.getTeacherId(),
                    lessonsGroupsDtoIn.getStudents()) ? true : null;
        }
        return true;
    }

    //метод возвращает диапазон дат занятий текущей группы
    public DateTimeRange dateRangeOfLessons() {
        if (lessons == null || lessons.isEmpty()) {
            return null;
        }
        //дата начала самого первого занятия
        LocalDate from = lessons
                .stream()
                .min(Comparator.comparing(Lesson::getStartDateTime))
                .get()
                .getStartDateTime()
                .toLocalDate();
        //дата начала самого последнего занятия
        LocalDate to = lessons
                .stream()
                .max(Comparator.comparing(Lesson::getStartDateTime))
                .get()
                .getStartDateTime()
                .toLocalDate();
        return new DateTimeRange(from, to);
    }

    //метод устанавливает значения полей занятий в списке в диапазоне дат
    public void setFieldsIntoLessons(Subject subject, Teacher teacher, Set<Student> students, DateTimeRange dateRange) {
        //проверяем и меняем поля в занятиях
        lessons.stream()
                //отбираем только те занятия, которые лежат в диапазоне редактирования группы
                .filter(lesson -> lesson.getStartDateTime().isAfter(dateRange.getFrom()) &&
                        lesson.getStartDateTime().isBefore(dateRange.getTo()))
                //проверяем и меняем поля в занятии
                .forEach(lesson -> lesson.setIfNotEquals(subject, teacher, students));
    }

    public Builder clone() {
        return new LessonsGroups.Builder()
                .setId(this.id)
                .setTitle(this.title)
                .setSchedule(this.schedule)
                .setLessons(this.lessons);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private long id;
        private String title;
        private String schedule;
        private Set<Lesson> lessons;

        public Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setSchedule(String schedule) {
            this.schedule = schedule;
            return this;
        }

        public Builder setLessons(Set<Lesson> lessons) {
            this.lessons = lessons;
            return this;
        }

        public LessonsGroups build() {
            return new LessonsGroups(id, title, schedule, lessons);
        }
    }
}
