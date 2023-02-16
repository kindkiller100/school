-- создаем таблицы
CREATE TABLE school_db.groups
(
    id serial PRIMARY KEY,
    title varchar(20) NOT NULL
);

CREATE TABLE school_db.groups_students
(
    group_id int NOT NULL REFERENCES school_db.groups(id),
    student_id int NOT NULL REFERENCES school_db.students(id)
);

-- заполняем таблицы тестовыми данными
INSERT INTO school_db.groups (title)
VALUES ('Новички'),
       ('Первая смена'),
       ('Вторая смена');

INSERT INTO school_db.groups_students(group_id, student_id)
VALUES (1, 2),
       (1, 4),
       (1, 5),
       (1, 8),
       (2, 1),
       (2, 2),
       (2, 3),
       (2, 5),
       (2, 9),
       (3, 4),
       (3, 6),
       (3, 7),
       (3, 8),
       (3, 10);


