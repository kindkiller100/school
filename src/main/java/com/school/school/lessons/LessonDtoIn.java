package com.school.school.lessons;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

public class LessonDtoIn {
    @Min(value = 0, message = "Id занятия должно быть положительным.")
    private long id;                    //id занятия
    @NotNull(message = "Дата начала занятия не может быть пустой.")
    private LocalDateTime startDateTime;//дата и время начала занятия
    @Min(value = 30, message = "Продолжительность занятия должна быть в диапазоне от 30 до 210 минут.")
    @Max(value = 210, message = "Продолжительность занятия должна быть в диапазоне от 30 до 210 минут.")
    private short duration;             //продолжительность занятия в минутах
    @Min(value = 1, message = "Id предмета должно быть больше нуля 0")
    private long subjectId;             //предмет
    @Min(value = 1, message = "Id преподавателя должно быть больше нуля 0")
    private long teacherId;             //преподаватель
    @Size(max = 40, message = "Подробное описание занятия не должно быть больше 40 символов.")
    private String description;
    private Long groupId;

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

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
}
