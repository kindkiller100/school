-- удаляем колонку id из таблицы student_lesson
ALTER TABLE school_db.student_lesson DROP COLUMN id;

-- добавляем primary key по обеим колонкам таблицы student_lesson
ALTER TABLE school_db.student_lesson
    ADD CONSTRAINT student_lesson_pkey
        PRIMARY KEY (student_id, lesson_id);

-- добавляем primary key по обеим колонкам таблицы groups_students
ALTER TABLE school_db.groups_students
    ADD CONSTRAINT groups_students_pkey
        PRIMARY KEY (group_id, student_id);
