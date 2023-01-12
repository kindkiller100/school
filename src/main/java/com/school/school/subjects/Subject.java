package com.school.school.subjects;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "subjects", schema="school_db")
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; //id subject
    @NotBlank
    @Size(min = 3, max = 20)
    private String title; //заголовок subject
    private String description; //описание subject
    private boolean deleted; //флаг "удаления" subject

    protected Subject() {
    }

    private Subject(long id, String title, String description, boolean deleted) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.deleted = deleted;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Subject)) return false;
        Subject subject = (Subject) o;
        return getId() == subject.getId()
                && getTitle().equals(subject.getTitle())
                && getDescription().equals(subject.getDescription())
                && Objects.equals(isDeleted(), subject.isDeleted());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getDescription());
    }

    @Override
    public String toString() {
        return "Subject{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", deleted=" + deleted +
                '}';
    }

    public Builder clone() {
        return new Subject.Builder()
                .setId(this.id)
                .setTitle(this.title)
                .setDescription(this.description)
                .setDeleted(this.deleted);
    }

    public static Builder builder() {
        return new Builder();
    }

    static public class Builder {
        private long id;
        private String title;
        private String description;
        private boolean deleted;

        public Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setDeleted(boolean deleted) {
            this.deleted = deleted;
            return this;
        }

        //возвращает объект внешнего класса с заданными параметрами
        public Subject build() {
            return new Subject(id, title, description, deleted);
        }
    }
}
