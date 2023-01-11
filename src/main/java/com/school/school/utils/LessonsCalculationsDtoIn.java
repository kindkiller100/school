package com.school.school.utils;

import javax.validation.constraints.Min;

//класс для получения параметров id (студента/преподавателя) и диапазона дат
public class LessonsCalculationsDtoIn {
    @Min(value = 1, message = "Must be greater than 1")
    private long id;    //id (студента/преподавателя)
    @CorrectDateTimeRange(message = "One or both dates are empty.")
    private DateTimeRange dateTimeRange;    //диапазона дат

    public LessonsCalculationsDtoIn(long id, DateTimeRange dateTimeRange) {
        this.id = id;
        this.dateTimeRange = dateTimeRange;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public DateTimeRange getDataTimeRange() {
        return dateTimeRange;
    }

    public void setDataTimeRange(DateTimeRange dateTimeRange) {
        this.dateTimeRange = dateTimeRange;
    }
}
