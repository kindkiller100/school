package com.school.school.lessons;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Lesson {
    private long id;
    private LocalDateTime startDateTime;
    private short duration;
    private long subjectId;
    private long teacherId;
    private String description;
    private boolean deleted;

    public Lesson(){}

    public Lesson(LocalDateTime startDateTime, short duration, long subjectId, long teacherId, String description, boolean deleted) {
        this.startDateTime = startDateTime;
        this.duration = duration;
        this.subjectId = subjectId;
        this.teacherId = teacherId;
        this.description = description;
        this.deleted = deleted;
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

    public boolean isDeleted() {
        return deleted;
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
