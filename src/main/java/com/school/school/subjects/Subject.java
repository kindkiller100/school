package com.school.school.subjects;

import com.school.school.students.Student;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "subjects")
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private String description;
    private boolean deleted;

    private Subject() {
    }

    private Subject(String title, String description, boolean deleted) {
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


    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "Subject{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
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

    public Builder clone() {
        return new Subject.Builder()
                .setId(this.id)
                .setTitle(this.title)
                .setDescription(this.description)
                .setDeleted(this.deleted);
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

        public Subject build() {//возвращает объект внешнего класса с заданными параметрами
            return new Subject(title, description, deleted);
        }
    }
}
