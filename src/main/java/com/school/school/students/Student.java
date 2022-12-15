package com.school.school.students;

import java.util.UUID;

public class Student {
    private UUID id;
    private String name;
    private Byte age;

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

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getAge() {
        return age;
    }

    public void setAge(Byte age) {
        this.age = age;
    }
}