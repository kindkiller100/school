package com.school.school.lessons;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

public class LessonDtoIn {
    @Min(value = 0, message = "Can't be negative")
    private long id;                    //id занятия
    @NotNull(message = "Start of lesson must not be empty")
    private LocalDateTime startDateTime;//дата и время начала занятия
    @Min(value = 30, message = "Duration of the lesson should be in the range from 30 to 210")
    @Max(value = 210, message = "Duration of the lesson should be in the range from 30 to 210")
    private short duration;             //продолжительность занятия в минутах
    @Min(value = 1, message = "Must be greater than 0")
    private long subjectId;             //предмет
    @Min(value = 1, message = "Must be greater than 0")
    private long teacherId;             //преподаватель
    @Size(max = 40, message = "Size of description must be less than 40 characters.")
    private String description;

    public LessonDtoIn() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public short getDuration() {
        return duration;
    }

    public void setDuration(short duration) {
        this.duration = duration;
    }

    public long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(long subjectId) {
        this.subjectId = subjectId;
    }

    public long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(long teacherId) {
        this.teacherId = teacherId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
