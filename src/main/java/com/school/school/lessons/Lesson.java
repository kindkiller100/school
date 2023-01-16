package com.school.school.lessons;

import com.school.school.subjects.Subject;
import com.school.school.teachers.Teacher;
import org.webjars.NotFoundException;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Entity
@Table(name = "lessons", schema="school_db")
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;                    //id занятия
    @NotNull(message = "Start of lesson must not be empty")
    @Column(name = "startdatetime")
    private LocalDateTime startDateTime;//дата и время начала занятия
    @Min(value = 30, message = "Duration of the lesson should be in the range from 30 to 210")
    @Max(value = 210, message = "Duration of the lesson should be in the range from 30 to 210")
    private short duration;             //продолжительность занятия в минутах
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private Subject subject;            //предмет
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private Teacher teacher;            //преподаватель
    @Size(max = 40, message = "Size of description must be less than 40 characters.")
    private String description;         //расшифровка (подробное описание) занятия

    //конструктор без параметров
    protected Lesson() {
    }

    //конструктор со всеми параметрами
    private Lesson(long id,
                   LocalDateTime startDateTime,
                   short duration,
                   Subject subject,
                   Teacher teacher,
                   String description) {
        this.id = id;
        this.startDateTime = startDateTime;
        this.duration = duration;
        this.subject = subject;
        this.teacher = teacher;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public short getDuration() {
        return duration;
    }

    public Subject getSubject() {
        return subject;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public String getDescription() {
        return description;
    }

    //переопределение метода equals(). Объекты одинаковы, если у них одинаковый id
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lesson lesson = (Lesson) o;
        return id == lesson.id;
    }

    //переопределение метода hashCode()
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    //переопределение метода toString()
    @Override
    public String toString() {
        return "Lesson{" +
                "subject: «" + subject.getTitle() +
                "», teacher: " + teacher.getFullName() +
                ", date of lesson: " + startDateTime.toLocalDate() +        //получаем дату занятия
                ", start at: " + startDateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")) +     //получаем время занятия в формате ЧЧ.ММ
                ", end at: " + startDateTime.toLocalTime().plusMinutes(duration).format(DateTimeFormatter.ofPattern("HH:mm")) + //получаем время занятия, добавляем продолжительность занятия и форматируем результат
                " (duration " + duration + " min.)" +
                ", description: '" + description + '\'' +
                '}';
    }

    //валидация даты начала занятия. Дата начала занятия не должна быть позже, чем день назад.
    public void startDateValidation() {
        if (startDateTime == null) {
            throw new NotFoundException("In lesson with id «" + id + "» date of start is null.");
        }
        if (startDateTime.toLocalDate().isBefore(LocalDate.now().minusDays(1))) {
            throw new NotFoundException("Lesson must start no later than one day ago.");
        }
    }

    public Builder clone() {
        return new Lesson.Builder()
                .setId(this.id)
                .setStartDateTime(this.startDateTime)
                .setDuration(this.duration)
                .setSubject(this.subject)
                .setTeacher(this.teacher)
                .setDescription(this.description);
    }

    static public class Builder {
        private long id;
        private LocalDateTime startDateTime;
        private short duration;
        private Subject subject;
        private Teacher teacher;
        private String description;

        public Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Builder setStartDateTime(LocalDateTime startDateTime) {
            this.startDateTime = startDateTime;
            return this;
        }

        public Builder setDuration(short duration) {
            this.duration = duration;
            return this;
        }

        public Builder setSubject(Subject subject) {
            this.subject = subject;
            return this;
        }

        public Builder setTeacher(Teacher teacher) {
            this.teacher = teacher;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Lesson build() { //возвращает объект внешнего класса с заданными параметрами
            return new Lesson(id,
                    startDateTime,
                    duration,
                    subject,
                    teacher,
                    description);
        }
    }
}
