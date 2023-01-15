package com.school.school.groups;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
@Table(name = "groups", schema = "school_db")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank
    @Size(max = 20)
    private String title;

    protected Group() {
    }

    private Group(long id, String title) {
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
        Group group = (Group) o;
        return id == group.id && Objects.equals(title, group.title);
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
        public Group build() {
            return new Group(id, title);
        }
    }
}
