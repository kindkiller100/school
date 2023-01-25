-- создание таблицы lessons_groups
-- формат schedule: 'день_недели,время_начала_занятия,продолжительность_занятия_в_минутах;...'
CREATE TABLE school_db.lessons_groups
(
    id serial PRIMARY KEY,
    title varchar(40) NOT NULL,
    schedule varchar(100)
);

-- заполнение таблицы lessons_groups тестовыми данными
INSERT INTO school_db.lessons_groups (title,schedule)
VALUES ('Рисование', '4,10:00,90'),
       ('Лепка', '6,09:00,135'),
       ('История искусства', '1,18:00,30');

-- добавление в таблицу lessons колонки group_id
ALTER TABLE school_db.lessons ADD COLUMN group_id int NULL REFERENCES school_db.lessons_groups(id);

-- добавление тестовых данных в таблицу lessons (изменение значений)
UPDATE school_db.lessons SET group_id = 1 WHERE id in (1,6);
UPDATE school_db.lessons SET group_id = 2 WHERE id in (3,8);
UPDATE school_db.lessons SET group_id = 3 WHERE id in (5,10);

-- добавление в таблицу student_lesson колонки visit
ALTER TABLE school_db.student_lesson ADD COLUMN visit boolean NOT NULL DEFAULT true;
