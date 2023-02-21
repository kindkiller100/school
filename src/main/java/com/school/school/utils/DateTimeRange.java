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

    //статический метод, определяющий минимальную из двух дат
    public static LocalDateTime min(LocalDateTime firstDate, LocalDateTime secondDate) {
        return firstDate.isBefore(secondDate) ? firstDate : secondDate;
    }

    //статический метод, определяющий максимальную из двух дат
    public static LocalDateTime max(LocalDateTime firstDate, LocalDateTime secondDate) {
        return firstDate.isAfter(secondDate) ? firstDate : secondDate;
    }

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

    //метод вилидаций объекта
    public void validateOrThrow() {
        //проверка, что начало диапазона меньше или равно конца диапазона
        if(!isValid()) {
            throw new NotFoundException(DateTimeRange.ERR_STRING);
        }
    }

    //метод проверки пересечения диапазонов дат
    public boolean intersection(DateTimeRange dateTimeRange) {
        ValidationException validationException = new ValidationException();
        if (dateTimeRange == null) {
            validationException.put("dateTimeRange", "Параметр метода не должен быть пустым.");
            validationException.throwExceptionIfIsNotEmpty();
        }
        validationException.put(dateTimeRange.validate(false));
        validationException.throwExceptionIfIsNotEmpty();

        return dateInRange(dateTimeRange.getFrom()) ||
                dateInRange(dateTimeRange.getTo()) ||
                dateTimeRange.dateInRange(from) ||
                dateTimeRange.dateInRange(to);
    }

    //метод проверяет принадлежность даты диапазону
    //возвращает true, если дата принадлежит диапазону
    private boolean dateInRange(LocalDateTime dateTime) {
        return (from.isEqual(dateTime) || from.isBefore(dateTime)) && (to.isEqual(dateTime) || to.isAfter(dateTime));
    }

    //метод вилидаций объекта
    public ValidationException validate(boolean flag) {
        ValidationException validationException = new ValidationException();
        //проверка, что начало и конец диапазона не null
        if(this.from == null | this.to == null){
            validationException.put("date_time_range", "Начало и конец диапазона не должны быть пустыми");
            return validationException;
        }
        //проверка, что начало диапазона меньше или равно конца диапазона
        if(!isValid()) {
            validationException.put("date_time_range", DateTimeRange.ERR_STRING);
        }
        if (flag && this.from.toLocalDate().isBefore(LocalDate.now().minusDays(1))) {
            validationException.put("dateRange.from", "Дата начала занятий должна быть не позднее, чем день назад.");
        }
        return validationException;
    }
}
