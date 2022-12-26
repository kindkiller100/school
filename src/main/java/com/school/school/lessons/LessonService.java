package com.school.school.lessons;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;

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
        //TODO: add validations
        if (lessonRepository.existsById(id)) {
            lessonRepository.deleteById(id);
        } else {
            throw new NotFoundException("Lesson with id «" + id + "» not found.");
        }
    }

    public void editById(Lesson editLesson){
        if (lessonRepository.existsById(editLesson.getId())){
            //TODO: add validations for id, subjectId and teacherId
            lessonRepository.save(editLesson);
        } else {
            //TODO: add custom exception
            throw new NotFoundException("Lesson with id «" + editLesson.getId() + "» not found.");
        }
    }
}
