package com.school.school.lessons;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Entity
@Table(name = "lessons", schema="school_db")
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;                    //id занятия

    @Column(name = "startdatetime")
    private LocalDateTime startDateTime;//дата и время начала занятия
    private short duration;             //продолжительность занятия в минутах
    @Column(name = "subject_id")
    private long subjectId;             //id предмета
    @Column(name = "teacher_id")
    private long teacherId;             //id преподавателя
    private String description;         //расшифровка (подробное описание) занятия

    //конструктор без параметров
    public Lesson(){}

    //конструктор со всеми параметрами кроме id
    public Lesson(LocalDateTime startDateTime, short duration, long subjectId, long teacherId, String description) {
        this.startDateTime = startDateTime;
        this.duration = duration;
        this.subjectId = subjectId;
        this.teacherId = teacherId;
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

    public long getSubjectId() {
        return subjectId;
    }

    public long getTeacherId() {
        return teacherId;
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
        //TODO: subjektId, teacherId -> name of subjekt & teacher
        return "Lesson{" +
                "subject: «" + subjectId +
                "», teacher: " + teacherId +
                ", date of lesson: " + startDateTime.toLocalDate() +        //получаем дату занятия
                ", start at: " + startDateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")) +     //получаем время занятия в формате ЧЧ.ММ
                ", end at: " + startDateTime.toLocalTime().plusMinutes(duration).format(DateTimeFormatter.ofPattern("HH:mm")) + //получаем время занятия, добавляем продолжительность занятия и форматируем результат
                " (duration " + duration + " min.)" +
                ", description: '" + description + '\'' +
                '}';
    }
}
