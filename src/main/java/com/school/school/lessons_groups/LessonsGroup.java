package com.school.school.lessons_groups;

import com.school.school.utils.CorrectSchedule;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
@Table(name = "lessons_groups", schema="school_db")
public class LessonsGroups {
    //id группы занятий
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    //название группы занятий
    @NotBlank(message = "Не должно быть пустым.")
    @Size(min = 5, message = "Длина названия группы занятий должна быть не менее 5 символов.")
    private String title;
    //расписание. Формат: 'день_недели,время_начала_занятия,продолжительность_занятия_в_минутах;...'
    @CorrectSchedule(message = "Расписание не соответствует нужному формату.")
    private String schedule;

    //конструктор без параметров
    protected LessonsGroups() {
    }

    //конструктор со всеми параметрами
    public LessonsGroups(long id, String title, String schedule) {
        this.id = id;
        this.title = title;
        this.schedule = schedule;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSchedule() {
        return schedule;
    }

    //переопределение метода equals()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LessonsGroups that = (LessonsGroups) o;
        return id == that.id;
    }

    //переопределение метода hashCode()
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    //переопределение метода toString()
    @Override
    public String toString() {
        return "LessonsGroups{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", schedule='" + schedule + '\'' +
                '}';
    }

    public Builder clone() {
        return new LessonsGroups.Builder()
                .setId(this.id)
                .setTitle(this.title)
                .setSchedule(this.schedule);
    }

    public static class Builder {
        private long id;
        private String title;
        private String schedule;

        public Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setSchedule(String schedule) {
            this.schedule = schedule;
            return this;
        }

        public LessonsGroups build() {
            return new LessonsGroups(id, title, schedule);
        }
    }
}
