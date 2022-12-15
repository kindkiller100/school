package com.school.school.students;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StudentsStorage {
    public static List<Student> data = new ArrayList<>();

    public static Student getById(UUID id) {
        if (id == null) {
            throw new NullPointerException("Student's id is empty.");
        }

        return StudentsStorage.data.stream()
                .filter(student -> student.getId().equals(id))
                .findAny()
                .orElseThrow(() -> new NullPointerException("Student by id '" + id + "' not found."));
    }
}