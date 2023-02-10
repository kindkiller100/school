package com.school.school.students_groups;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
@Table(name = "students_groups", schema = "school_db")
public class StudentsGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank
    @Size(min = 1, max = 20)
    private String title;

    protected StudentsGroup() {
    }

    private StudentsGroup(long id, String title) {
        this.id = id;
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentsGroup studentsGroup = (StudentsGroup) o;
        return id == studentsGroup.id && Objects.equals(title, studentsGroup.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }

    public Builder clone() {
        return new Builder()
                .setId(this.id)
                .setTitle(this.title);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private long id;
        private String title;

        public Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        //возвращает объект внешнего класса с заданными параметрами
        public StudentsGroup build() {
            return new StudentsGroup(id, title);
        }
    }
}
