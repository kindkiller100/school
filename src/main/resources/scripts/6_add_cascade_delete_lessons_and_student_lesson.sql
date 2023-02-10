-- добавляем удаление каскадом записей из таблицы student_lesson при удалении записи из таблицы lessons
ALTER TABLE school_db.student_lesson
DROP
CONSTRAINT student_lesson_lesson_id_fkey;

ALTER TABLE school_db.student_lesson
    ADD CONSTRAINT student_lesson_lesson_id_fkey
        FOREIGN KEY (lesson_id) REFERENCES school_db.lessons (id)
            ON DELETE CASCADE;
