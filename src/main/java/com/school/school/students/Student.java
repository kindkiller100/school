package com.school.school.students;

import java.time.LocalDate;

public class Student {
    private long id;
    private String name;
    private String secondName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;
    private String telephoneNumber;
    private String info;
    private boolean deleted;

    private Student() {
    }

    private Student(String name,
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

    static public class Builder {
        private String name;
        private String secondName;
        private String lastName;
        private LocalDate dateOfBirth;
        private String gender;
        private String telephoneNumber;
        private String info;
        private boolean deleted;

        public Student.Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Student.Builder setSecondName(String secondName) {
            this.secondName = secondName;
            return this;
        }

        public Student.Builder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Student.Builder setDateOfBirth(LocalDate dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
            return this;
        }

        public Student.Builder setGender(String gender) {
            this.gender = gender;
            return this;
        }

        public Student.Builder setTelephoneNumber(String telephoneNumber) {
            this.telephoneNumber = telephoneNumber;
            return this;
        }

        public Student.Builder setInfo(String info) {
            this.info = info;
            return this;
        }

        public Student.Builder setDeleted(boolean deleted) {
            this.deleted = deleted;
            return this;
        }

        public Student build() {
            return new Student(name,
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
