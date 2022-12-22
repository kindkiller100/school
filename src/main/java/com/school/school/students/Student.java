package com.school.school.students;

import java.time.LocalDate;

public class Student {
    private long id;
    private String name;
    private String secondName;
    private String lastname;
    private LocalDate dateOfBirth;
    private String gender;
    private String telephoneNumber;
    private String info;
    private boolean deleted;

    public void generateId(){
//        long countOfId;
//        UUID newId;
//        do {
//            newId = UUID.randomUUID();
//            UUID finalNewId = newId;
//            countOfId = StudentsStorage.data
//                    .stream()
//                    .filter(student -> student.getId().equals(finalNewId))
//                    .count();
//        } while (countOfId > 0);
//        this.id = newId;
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

    public String getLastname() {
        return lastname;
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
