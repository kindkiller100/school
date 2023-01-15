package com.school.school.teachers;

import com.school.school.students.Student;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "teachers", schema = "school_db")
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "Не должно быть пустым")
    @Size(min = 3, max = 20, message = "Длина name должна быть от 3 до 20 символов")
    private String name;
    @Size(min = 3, max = 20, message = "Длина name должна быть от 3 до 20 символов")
    @Column(name = "secondname")
    private String secondName;
    @NotBlank(message = "Не должно быть пустым")
    @Size(min = 3, max = 20, message = "Длина name должна быть от 3 до 20 символов")
    @Column(name = "lastname")
    private String lastName;
    @Column(name = "dateofbirth")
    private LocalDate dateOfBirth;
    private String gender;
    @NotBlank(message = "Не должно быть пустым")
    @Pattern(regexp = "^[+0-9]{1}[0-9]{5,19}$",
            message = "Номера начинаются со знака '+' или цифры, должны иметь от 6 до 20 символов")
    @Column(name = "telephonenumber")
    private String telephoneNumber;
    private String info;
    private boolean deleted;

    private Teacher() {
    }

    private Teacher(long id,
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

    public Builder clone() {
        return new Teacher.Builder()
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

        public Teacher build() {//возвращает объект внешнего класса с заданными параметрами
            return new Teacher(id,
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

