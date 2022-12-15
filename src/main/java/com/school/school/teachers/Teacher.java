package com.school.school.teachers;

import java.time.LocalDate;

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
