package com.school.school.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.school.school.exceptions.ValidationException;
import org.webjars.NotFoundException;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class DateTimeRange {
    //сообщение об ошибке диапазона дат
    public static final String ERR_STRING = "Указан недопустимый диапазон дат. Дата окончания диапазона больше, чем дата начала диапазона.";
    @NotNull(message = "Начало диапазона не должно быть пустым")
    private LocalDateTime from;
    @NotNull(message = "Конец диапазона не должен быть пустым")
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
    @JsonIgnore
    public boolean isValid() {
        //проверка, что начало диапазона меньше или равно конца диапазона
        //true - если диапазон дат корректный
        return !this.from.isAfter(this.to);
    }
}
