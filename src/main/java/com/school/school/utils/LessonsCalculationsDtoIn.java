package com.school.school.utils;

import javax.validation.constraints.Min;

//класс для получения параметров id (студента/преподавателя) и диапазона
public class LessonsCalculationsDtoIn {
    @Min(value = 0, message = "Должно быть больше 0")
    private long id;    //id (студента/преподавателя)
    @CorrectDateTimeRange(message = "Одна или обе даты пусты.")
    private DateRange dateRange;    //диапазона дат

    public LessonsCalculationsDtoIn(long id, DateRange dateRange) {
        this.id = id;
        this.dateRange = dateRange;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public DateRange getDateTimeRange() {
        return dateRange;
    }

    public void setDateTimeRange(DateRange dateRange) {
        this.dateRange = dateRange;
    }
}
