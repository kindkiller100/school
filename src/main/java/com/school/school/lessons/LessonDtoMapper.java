package com.school.school.lessons;

import com.school.school.students.Student;
import com.school.school.students.StudentService;
import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

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
        TypeMap<LessonDtoIn, Lesson> typeMap = modelMapper.getTypeMap(LessonDtoIn.class, Lesson.class);

        if (typeMap == null) {
            typeMap = modelMapper.createTypeMap(LessonDtoIn.class, Lesson.class);
        }

        class StudentIdsToStudentsConverter extends AbstractConverter<Set<Long>, Set<Student>> {
            @Override
            protected Set<Student> convert(Set<Long> studentIds) {
                return studentIds
                        .stream()
                        .map(id -> studentService.getIfExists(id))
                        .collect(Collectors.toSet());
            }
        }

        typeMap.addMappings(mapper -> mapper.using(new StudentIdsToStudentsConverter())
                .map(LessonDtoIn::getStudentIds, Lesson::setStudents));

        //мапим DTO-объект в объект класса Lesson и возвращаем в качестве результата
        //результатом маппинга будет объект класса Lesson, в котором у полей subject и teacher будут заполнены только id
        return modelMapper.map(lessonDto, Lesson.class);
    }
}
