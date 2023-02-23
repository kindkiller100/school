package com.school.school.lessons;

import com.school.school.students.StudentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class LessonDtoMapper {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private StudentService studentService;

    public Lesson convertDtoToEntity(LessonDtoIn lessonDto, boolean createFlag) {
        //если это создание нового предмета, то устанавливаем Id = 0, при записи БД сама подберет нужный Id
        if (createFlag) {
            lessonDto.setId(0L);
        }

        //настраиваем маппер для конвертации Set<Long> studentsIds в Set<Student> students
        modelMapper.typeMap(LessonDtoIn.class, Lesson.class).addMappings(
                mapper -> mapper.using(context -> studentService.getAllById((Set<Long>) context.getSource()))
                        .map(LessonDtoIn::getStudentIds, Lesson::setStudents)
        );

        //мапим DTO-объект в объект класса Lesson и возвращаем в качестве результата
        //результатом маппинга будет объект класса Lesson, в котором у полей subject и teacher будут заполнены только id
        return modelMapper.map(lessonDto, Lesson.class);
    }
}
