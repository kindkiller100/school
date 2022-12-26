package com.school.school.lessons;

import com.school.school.students.Student;

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

    private Lesson() {
    }

    private Lesson(long id,
                   LocalDateTime startDateTime,
                   short duration,
                   long subjectId,
                   long teacherId,
                   String description) {
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

    public Builder clone() {
        return new Lesson.Builder()
                .setId(this.id)
                .setStartDateTime(this.startDateTime)
                .setDuration(this.duration)
                .setSubjectId(this.subjectId)
                .setTeacherId(this.teacherId)
                .setDescription(this.description);
    }

    static public class Builder {
        private long id;
        private LocalDateTime startDateTime;
        private short duration;
        private long subjectId;
        private long teacherId;
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

        public Builder setSubjectId(long subjectId) {
            this.subjectId = subjectId;
            return this;
        }

        public Builder setTeacherId(long teacherId) {
            this.teacherId = teacherId;
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
                    subjectId,
                    teacherId,
                    description);
        }
    }
}
