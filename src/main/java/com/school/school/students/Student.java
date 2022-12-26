package com.school.school.students;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @Column(name = "secondname")
    private String secondName;
    @Column(name = "lastname")
    private String lastName;
    @Column(name = "dateofbirth")
    private LocalDate dateOfBirth;
    private String gender;
    @Column(name = "telephonenumber")
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

    public String getLastname() {
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
