package com.school.school.lessons;

import com.school.school.students.Student;
import com.school.school.students.StudentService;
import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
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
        //конфигурируем маппер: стратегия - стандартная, совпадение по полям, уровень доступа полей - приватные
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STANDARD)
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);
        //если это создание нового предмета, то устанавливаем Id = 0, при записи БД сама подберет нужный Id
        if (createFlag) {
            lessonDto.setId(0L);
        }

        //настраиваем маппер для конвертации <Set<Long> studentsIds в Set<Student> students
        //TODO: в примере добавление typeMap выносили в конструктор,
        //TODO: но здесь так не получилось - выдает ошибку, что modelMapper не определен
        TypeMap<LessonDtoIn, Lesson> typeMap = modelMapper.getTypeMap(LessonDtoIn.class, Lesson.class);

        if (typeMap == null) {
            typeMap = modelMapper.createTypeMap(LessonDtoIn.class, Lesson.class);
        }

        //конвертер <Set<Long> studentsIds в Set<Student> students
        class StudentIdsToStudentsConverter extends AbstractConverter<Set<Long>, Set<Student>> {
            @Override
            protected Set<Student> convert(Set<Long> studentIds) {
                return studentIds
                        .stream()
                        .map(id -> studentService.getIfExists(id))
                        .collect(Collectors.toSet());
            }
        }

        //TODO: здесь ошибка, ругается на отсутствие сеттера, если добавить setStudents() в Lesson
        //TODO: и поменять на .map(LessonDtoIn::getStudentIds, Lesson::setStudents), то работает
        typeMap.addMappings(mapper -> mapper.using(new StudentIdsToStudentsConverter())
                .<Set<Student>>map(LessonDtoIn::getStudentIds, (lesson, students) -> {
                    lesson.clone()
                            .setStudents(students)
                            .build();
                }));

        //мапим DTO-объект в объект класса Lesson и возвращаем в качестве результата
        //результатом маппинга будет объект класса Lesson, в котором у полей subject и teacher будут заполнены только id
        return modelMapper.map(lessonDto, Lesson.class);
    }
}
