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

    private Student() {
    }

    private Student(long id,
                    String name,
                    String secondName,
                    String lastName,
                    LocalDate dateOfBirth,
                    String gender,
                    String telephoneNumber,
                    String info,
                    boolean deleted) {
        this.name = name;
        this.secondName = secondName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.telephoneNumber = telephoneNumber;
        this.info = info;
        this.deleted = deleted;
    }

    public void generateId() {
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

    public Builder clone() {
        return new Student.Builder()
                .setId(this.id)
                .setName(this.name)
                .setSecondName(this.secondName)
                .setLastName(this.lastName)
                .setDateOfBirth(this.dateOfBirth)
                .setGender(this.gender)
                .setTelephoneNumber(this.telephoneNumber)
                .setInfo(this.info)
                .setDeleted(this.deleted);
    }

    static public class Builder {
        private long id;
        private String name;
        private String secondName;
        private String lastName;
        private LocalDate dateOfBirth;
        private String gender;
        private String telephoneNumber;
        private String info;
        private boolean deleted;

        public Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setSecondName(String secondName) {
            this.secondName = secondName;
            return this;
        }

        public Builder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder setDateOfBirth(LocalDate dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
            return this;
        }

        public Builder setGender(String gender) {
            this.gender = gender;
            return this;
        }

        public Builder setTelephoneNumber(String telephoneNumber) {
            this.telephoneNumber = telephoneNumber;
            return this;
        }

        public Builder setInfo(String info) {
            this.info = info;
            return this;
        }

        public Builder setDeleted(boolean deleted) {
            this.deleted = deleted;
            return this;
        }

        public Student build() {//возвращает объект внешнего класса с заданными параметрами
            return new Student(id,
                    name,
                    secondName,
                    lastName,
                    dateOfBirth,
                    gender,
                    telephoneNumber,
                    info,
                    deleted);
        }
    }
}
