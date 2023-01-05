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
        //TODO: add validations
        lessonRepository.save(lesson);
    }

    public void delete(long id){
        if (lessonRepository.existsById(id)) {      //проверяем, есть ли запись с таким id в базе данных
            lessonRepository.deleteById(id);        //удаляем запись по id
        } else {                                    //если записи нет - выбрасываем ошибку
            throw new NotFoundException("Lesson with id «" + id + "» not found.");
        }
    }

    public void editById(Lesson editLesson){
        if (lessonRepository.existsById(editLesson.getId())){   //проверяем, есть ли запись с таким id в базе данных
            //TODO: add validations for id, subjectId and teacherId
            lessonRepository.save(editLesson);                  //сохраняем запись с измененными данными в БД
        } else {                                                //иначе выбрасываем ошибку
            //TODO: add custom exception
            throw new NotFoundException("Lesson with id «" + editLesson.getId() + "» not found.");
        }
    }
}
