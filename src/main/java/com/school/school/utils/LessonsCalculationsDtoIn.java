package com.school.school.utils;

import javax.validation.constraints.Min;

//класс для получения параметров id (студента/преподавателя) и диапазона
public class LessonsCalculationsDtoIn {
    @Min(value = 0, message = "Должно быть больше 0")
    private long id;    //id (студента/преподавателя)
    @CorrectDateTimeRange(message = "Одна или обе даты пусты.")
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

    public DateTimeRange getDateTimeRange() {
        return dateTimeRange;
    }

    public void setDateTimeRange(DateTimeRange dateTimeRange) {
        this.dateTimeRange = dateTimeRange;
    }
}
