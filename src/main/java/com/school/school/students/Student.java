package com.school.school.students;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;


public class Student {
    private UUID id;
    private String name;
    private Byte age;

    public UUID getId() {
        return id;
    }

    public void generateId() {
        this.id = UUID.randomUUID();
    }

    public String getName() {
        return name;
    }

    public Byte getAge() {
        return age;
    }
}
