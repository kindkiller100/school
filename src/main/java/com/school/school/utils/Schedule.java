package com.school.school.utils;

import com.school.school.exceptions.ValidationException;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Schedule {
    //день недели (1...7)
    private byte dayOfWeek;
    //время начала занятия
    private String timeOfStart;
    //продолжительность занятия в минутах (от 30 до 210)
    private short duration;

    public static Schedule convertToSchedule(String str) {
        //TODO: добавить сообщения об ошибках
        if (str == null) {
            //сообщение об ошибке
            return null;
        }
        if (!str.matches("[1-7],([01][0-9]|2[0-3]):[0-5][0-9],([3-9][0-9]|1[0-9][0-9]|2[01][0-9])")) {
            //сообщение об ошибке
            return null;
        }
        String[] arrayOfString = str.split(",");
        return new Schedule(Byte.parseByte(arrayOfString[0]), arrayOfString[1], Short.parseShort(arrayOfString[2]));
    }

    public static List<Schedule> convertToListOfSchedules(String str) {
        //TODO: добавить сообщения об ошибках
        if (str == null) {
            //сообщение об ошибке
            return null;
        }
        if (!str.matches("[1-7],([01][0-9]|2[0-3]):[0-5][0-9],([3-9][0-9]|1[0-9][0-9]|2[01][0-9])" +
                "(;[1-7],([01][0-9]|2[0-3]):[0-5][0-9],([3-9][0-9]|1[0-9][0-9]|2[01][0-9])){0,6}")) {
            //сообщение об ошибке
            return null;
        }
        String[] arrayOfString = str.split(";");
        List<Schedule> list = new ArrayList<>();
        for (String s : arrayOfString) {
            list.add(Schedule.convertToSchedule(s));
        }
        return list;
    }

    public static String createStringOfSchedules(List<Schedule> schedules) {
        String str = new String();
        if (schedules == null) {
            return str;
        }
        if (schedules.size() > 0) {
            str = schedules.stream()
                    .map(Schedule::convertToString)
                    .collect(Collectors.joining(";"));
        }
        return str;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Schedule schedule = (Schedule) o;
        return dayOfWeek == schedule.dayOfWeek && duration == schedule.duration && Objects.equals(timeOfStart, schedule.timeOfStart);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dayOfWeek, timeOfStart, duration);
    }

    public ValidationException validate(int indexOfList) {
        String index = indexOfList >= 0 ? String.valueOf(indexOfList) : "";
        ValidationException validationException = new ValidationException();
        if (this.dayOfWeek < 1 || this.dayOfWeek > 7) {
            validationException.put("dayOfWeek:" + index, "Номер дня недели должен быть от 1 до 7 включительно.");
        }
        if (this.timeOfStart == null) {
            validationException.put("timeOfStart:" + index, "Время начала занятия не должно быть пустым.");
        } else if (!this.timeOfStart.matches("([01][0-9]|2[0-3]):[0-5][0-9]")) {
            validationException.put("timeOfStart:" + index, "Время начала занятия не соответствует формату чч:мм.");
        }
        if (this.duration < 30 || this.duration > 210) {
            validationException.put("duration:" + index, "Продолжительность занятия должна быть в пределах от 30 до 210 минут.");
        }
        return validationException;
    }
}
