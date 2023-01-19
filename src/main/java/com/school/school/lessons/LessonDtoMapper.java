package com.school.school.lessons;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LessonDtoMapper {
    @Autowired
    private ModelMapper modelMapper;

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
        //мапим DTO-объект в объект класса Lesson и возвращаем в качестве результата
        //результатом маппинга будет объект класса Lesson, в котором у полей subject и teacher будут заполнены только id
        return modelMapper.map(lessonDto, Lesson.class);
    }
}
