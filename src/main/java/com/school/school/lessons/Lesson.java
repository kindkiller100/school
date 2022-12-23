package com.school.school.lessons;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Entity
@Table(name = "lessons")
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "startdatetime")
    private LocalDateTime startDateTime;
    private short duration;
    @Column(name = "subject_id")
    private long subjectId;
    @Column(name = "teacher_id")
    private long teacherId;
    private String description;

    public Lesson(){}

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lesson lesson = (Lesson) o;
        return id == lesson.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        // subjektId, teacherId
        return "Lesson{" +
                "subject: «" + subjectId +
                "», teacher: " + teacherId +
                ", date of lesson: " + startDateTime.toLocalDate() +
                ", start at: " + startDateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")) +
                ", end at: " + startDateTime.toLocalTime().plusMinutes(duration).format(DateTimeFormatter.ofPattern("HH:mm")) +
                " (duration " + duration + " min.)" +
                ", description: '" + description + '\'' +
                '}';
    }
}
