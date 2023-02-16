-- добавляем удаление каскадом записей из таблицы student_lesson при удалении записи из таблицы students
ALTER TABLE school_db.student_lesson
DROP
CONSTRAINT student_lesson_student_id_fkey;

ALTER TABLE school_db.student_lesson
    ADD CONSTRAINT student_lesson_student_id_fkey
        FOREIGN KEY (student_id) REFERENCES school_db.students (id)
            ON DELETE CASCADE;