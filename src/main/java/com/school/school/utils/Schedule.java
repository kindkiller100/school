package com.school.school.utils;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Schedule {
    //день недели (1...7)
    private byte dayOfWeek;
    //время начала занятия
    private String timeOfStart;
    //продолжительность занятия в минутах (от 30 до 210)
    private short duration;

    public Schedule() {}

    public Schedule(byte dayOfWeek, String timeOfStart, short duration) {
        this.dayOfWeek = dayOfWeek;
        this.timeOfStart = timeOfStart;
        this.duration = duration;
    }

    public byte getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(byte dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getTimeOfStart() {
        return timeOfStart;
    }

    public void setTimeOfStart(String timeOfStart) {
        this.timeOfStart = timeOfStart;
    }

    public short getDuration() {
        return duration;
    }

    public void setDuration(short duration) {
        this.duration = duration;
    }

    //функция создания диапазона дат определенного для недели с установкой времени
    public List<LocalDateTime> createListOfDate(DateTimeRange dateRange) {
        //обе даты лиапазона приводим к типу LocalDate
        //вызываем метод datesUntil(), который запускает поток от даты начала диапазона до даты конца диапазона
        return dateRange.getFrom().toLocalDate().datesUntil(dateRange.getTo().toLocalDate().plusDays(1))
                //фильтруем даты по дню недели
                .filter(date -> date.getDayOfWeek() == DayOfWeek.of(dayOfWeek))
                //преобразовываем LocalDate в LocalDateTime и добавляем время начала занятия
                .map(date -> date.atTime(LocalTime.parse(timeOfStart, DateTimeFormatter.ofPattern("HH:mm"))))
                //преобразовываем результат в List<LocalDateTime>
                .toList();
    }

    public String convertToString() {
        return dayOfWeek + "," +
                timeOfStart + "," +
                duration;
    }
}
