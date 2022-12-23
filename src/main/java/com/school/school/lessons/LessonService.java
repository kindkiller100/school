package com.school.school.lessons;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class LessonService {
    @Autowired
    private LessonRepository lessonRepository;

    public List<Lesson> list(){
        return lessonRepository.findAll();
    }

    public void create(Lesson lesson){
        lessonRepository.save(lesson);
    }

    public void delete(long id){
        if (lessonRepository.existsById(id)) {
            lessonRepository.deleteById(id);
        }
    }

    public void editById(long id, Lesson editLesson){
        if (id != editLesson.getId())
            throw new NullPointerException("Id dont equals.");
        if (lessonRepository.existsById(id)){
            //need validates for id, subjectId and teacherId
            lessonRepository.save(editLesson);
        } else {
            //need custom exception
            throw new NullPointerException("Don`t found a lesson by id «" + id + "».");
        }
    }
}
