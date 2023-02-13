package com.school.school.lessons_groups;

import com.school.school.utils.DateTimeRange;
import com.school.school.utils.Schedule;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class LessonsGroupsDtoIn {
    @Min(value = 1, message = "Id предмета должно быть больше нуля.")
    private long subjectId;
    @Min(value = 1, message = "Id преподавателя должно быть больше нуля.")
    private long teacherId;
    @Min(value = 0, message = "Id группы занятий должно быть положительным.")
    private long lessonsGroupId;
    @NotBlank(message = "Название группы занятий не должно быть пустым.")
    @Size(min = 5, message = "Длина названия группы занятий должна быть не менее 5 символов.")
    private String lessonsGroupTitle;
    private Set<Long> students;
    private DateTimeRange dateRange;
    private List<Schedule> schedules;

    public LessonsGroupsDtoIn() {}

    public LessonsGroupsDtoIn(long subjectId, long teacherId, long lessonsGroupId, String lessonsGroupTitle, Set<Long> students, DateTimeRange dateRange, List<Schedule> schedules) {
        this.subjectId = subjectId;
        this.teacherId = teacherId;
        this.lessonsGroupId = lessonsGroupId;
        this.lessonsGroupTitle = lessonsGroupTitle;
        this.students = students;
        this.dateRange = dateRange;
        this.schedules = schedules;
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

    public long getLessonsGroupId() {
        return lessonsGroupId;
    }

    public void setLessonsGroupId(long lessonsGroupId) {
        this.lessonsGroupId = lessonsGroupId;
    }

    public String getLessonsGroupTitle() {
        return lessonsGroupTitle;
    }

    public void setLessonsGroupTitle(String lessonsGroupTitle) {
        this.lessonsGroupTitle = lessonsGroupTitle;
    }

    public Set<Long> getStudents() {
        return students;
    }

    public void setStudents(Set<Long> students) {
        this.students = students;
    }

    public DateTimeRange getDateRange() {
        return dateRange;
    }

    public void setDateRange(DateTimeRange dateRange) {
        this.dateRange = dateRange;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }
}
