package com.school.school.utils;

import com.school.school.lessons.Lesson;
import com.school.school.lessons.LessonRepository;
import com.school.school.subjects.SubjectRepository;
import com.school.school.teachers.TeacherRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

@Service
public class LessonDtoInService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private TeacherRepository teacherRepository;

    private static StringBuilder errString = new StringBuilder();

    public Lesson convertDtoToEntity(LessonDtoIn lessonDto, boolean createFlag) {
        //конфигурируем маппер: стратегия - стандартная, совпадение по полям, уровень доступа полей - приватные
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STANDARD)
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);
        //проверяем объект DTO
        validate(lessonDto, createFlag);
        //мапим DTO-объект в объект класса Lesson и возвращаем в качестве результата
        return modelMapper.map(lessonDto, Lesson.class);
    }

    private void validate(LessonDtoIn lessonDto, boolean createFlag) {
        //очищаем строку ошибок
        errString.setLength(0);
        //если это создание нового предмета, то устанавливаем Id = 0, при записи БД сама подберет нужный Id
        if (createFlag) {
            lessonDto.setId(0L);
        } else {
            //если это редактирование существующего предмета, то проверяем наличие в БД записи по такому Id
            if (!lessonRepository.existsById(lessonDto.getId())) {
                errString.append("Lesson with id «" + lessonDto.getId() + "» not found.");
            }
        }
        //проверяем существует ли предмет с указанным Id
        if (!subjectRepository.existsById(lessonDto.getSubjectId())) {
            errString.append("Subject with id «" + lessonDto.getSubjectId() + "» not found.");
        }
        //проверяем существует ли преподаватель с указанным Id
        if (!teacherRepository.existsById(lessonDto.getTeacherId())) {
            errString.append("Teacher with id «" + lessonDto.getTeacherId() + "» not found.");
        }
        //если строка не пустая, то выбрасываем эксэпшн
        if (!errString.isEmpty()) {
            throw new NotFoundException(errString.toString());
        }
    }
}
