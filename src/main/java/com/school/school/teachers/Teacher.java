package com.school.school.teachers;

import java.time.LocalDate;
import java.util.Objects;

public class Teacher {
    private long id;
    private String name;
    private String secondName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;
    private String telephoneNumber;
    private String info;
    private boolean deleted;

    public Teacher() {
    }

    public Teacher(long id,
                   String name,
                   String secondName,
                   String lastName,
                   LocalDate dateOfBirth,
                   String gender,
                   String telephoneNumber,
                   String info,
                   boolean deleted) {
        this.id = id;
        this.name = name;
        this.secondName = secondName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.telephoneNumber = telephoneNumber;
        this.info = info;
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Teacher teacher = (Teacher) o;
        return id == teacher.id
                && deleted == teacher.deleted
                && name.equals(teacher.name)
                && Objects.equals(secondName, teacher.secondName)
                && lastName.equals(teacher.lastName)
                && Objects.equals(dateOfBirth, teacher.dateOfBirth)
                && Objects.equals(gender, teacher.gender)
                && telephoneNumber.equals(teacher.telephoneNumber)
                && Objects.equals(info, teacher.info);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,
                name,
                secondName,
                lastName,
                dateOfBirth,
                gender,
                telephoneNumber,
                info,
                deleted);
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", secondName='" + secondName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", gender='" + gender + '\'' +
                ", telephoneNumber='" + telephoneNumber + '\'' +
                ", info='" + info + '\'' +
                ", deleted=" + deleted +
                '}';
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSecondName() {
        return secondName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public String getInfo() {
        return info;
    }

    public boolean isDeleted() {
        return deleted;
    }
}
