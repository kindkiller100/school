package com.school.school.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.school.school.exceptions.ValidationException;
import org.webjars.NotFoundException;

import java.time.LocalDate;

public class DateRange {
    //сообщение об ошибке диапазона дат
    public static final String ERR_STRING = "Указан недопустимый диапазон дат. Дата окончания диапазона больше, чем дата начала диапазона.";
    private LocalDate from;
    private LocalDate to;

    public DateRange(){}

    public DateRange(LocalDate from, LocalDate to) {
        this.from = from;
        this.to = to;
    }

    public LocalDate getFrom() {
        return from;
    }

    public void setFrom(LocalDate from) {
        this.from = from;
    }

    public LocalDate getTo() {
        return to;
    }

    public void setTo(LocalDate to) {
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
            throw new NotFoundException(DateRange.ERR_STRING);
        }
    }

    //метод вилидаций объекта
    public ValidationException validate() {
        ValidationException validationException = new ValidationException();
        //проверка, что начало и конец диапазона не null
        if(this.from == null | this.to == null){
            validationException.put("date_range", "Начало и конец диапазона не должны быть пустыми");
            return validationException;
        }
        //проверка, что начало диапазона меньше или равно конца диапазона
        if(this.from.isAfter(this.to)) {
            validationException.put("date_range", DateRange.ERR_STRING);
        }
        return validationException;
    }
}
