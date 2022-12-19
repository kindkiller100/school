package com.school.school.students;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

public class StudentsStorage {
    public static List<Student> data = new ArrayList<>();

    public static void create (Student student) {
        if (student.getId() == 0) {
            student.generateId();
        }
        data.add(student);
    }
    public static List<Student> getAll(){
        return data;
    }
    public static Student getById(Long id) {
        if (id == null) {
            throw new NullPointerException("Student's id is empty.");
        }

        return StudentsStorage.data.stream()
                .filter(student -> Objects.equals(student.getId(), id))
                .findAny()
                .orElseThrow(() -> new NullPointerException("Student by id '" + id + "' not found."));
    }
    public static void update (Long id, Student student){
        if (!id.equals(student.getId())){
            throw new RuntimeException("Selected id not equals student body id");
        }
        data.set(getIndexById(id), student);
    }
    public static void delete (Long id){
        data.remove(getIndexById(id));
    }
    private static int getIndexById (Long id){
        return IntStream.range(0, data.size())
                .filter(i -> id.equals(data.get(i).getId()))
                .findFirst()
                .orElseThrow();
    }


}
