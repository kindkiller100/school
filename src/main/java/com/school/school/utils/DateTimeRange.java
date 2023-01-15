package com.school.school.utils;

import org.webjars.NotFoundException;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class DateTimeRange {
    //сообщение об ошибке диапазона дат
    public static final String errString = "Invalid date range specified. The end date of the range is greater than the start date of the range.";
    @NotNull(message = "Start range must not be empty")
    private LocalDateTime from;
    @NotNull(message = "End range must not be empty")
    private LocalDateTime to;

    public DateTimeRange(){}

    public DateTimeRange(LocalDate from, LocalDate to) {
        this.from = LocalDateTime.of(from, LocalTime.MIN);
        this.to = LocalDateTime.of(to, LocalTime.MAX);
    }

    public DateTimeRange(LocalDateTime from, LocalDateTime to) {
        this.from = from;
        this.to = to;
    }

    public LocalDateTime getFrom() {
        return from;
    }

    public void setFrom(LocalDateTime from) {
        this.from = from;
    }

    public LocalDateTime getTo() {
        return to;
    }

    public void setTo(LocalDateTime to) {
        this.to = to;
    }

    //метод вилидаций объекта
    public boolean validation() {
        //проверка, что начало диапазона меньше или равно конца диапазона
        //true - если диапазон дат корректный
        return !this.from.isAfter(this.to);
    }

    //метод вилидаций объекта
    public void rangeValidation() {
        //проверка, что начало диапазона меньше или равно конца диапазона
        if(this.from.isAfter(this.to)) {
            throw new NotFoundException(DateTimeRange.errString);
        }
    }
}
