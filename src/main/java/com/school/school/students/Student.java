package com.school.school.students;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class Student {
    private UUID id;
    private String name;
    private Byte age;

    public UUID getId() {
        return id;
    }

    public void generateId(){
        long countOfId;
        UUID newId;
        do {
            newId = UUID.randomUUID();
            UUID finalNewId = newId;
            countOfId = StudentsStorage.data
                    .stream()
                    .filter(student -> student.getId().equals(finalNewId))
                    .count();
        } while (countOfId > 0);
        this.id = newId;
    }


    public String getName() {
        return name;
    }

    public Byte getAge() {
        return age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(Byte age) {
        this.age = age;
    }
}
