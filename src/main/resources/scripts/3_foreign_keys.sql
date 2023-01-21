-- Таблица lessons
ALTER TABLE school_db.lessons DROP CONSTRAINT lessons_subject_id_fkey,
    ADD CONSTRAINT lessons_subject_id_fkey FOREIGN KEY (subject_id) REFERENCES school_db.subjects(id);
ALTER TABLE school_db.lessons DROP CONSTRAINT lessons_teacher_id_fkey,
    ADD CONSTRAINT lessons_teacher_id_fkey FOREIGN KEY (teacher_id) REFERENCES school_db.teachers(id);

-- Таблица student_lesson
ALTER TABLE school_db.student_lesson DROP CONSTRAINT student_lesson_lesson_id_fkey,
    ADD CONSTRAINT student_lesson_lesson_id_fkey FOREIGN KEY (lesson_id) REFERENCES school_db.lessons(id);
ALTER TABLE school_db.student_lesson DROP CONSTRAINT student_lesson_student_id_fkey,
    ADD CONSTRAINT student_lesson_student_id_fkey FOREIGN KEY (student_id) REFERENCES school_db.students(id);

-- Таблица payments
ALTER TABLE school_db.payments DROP CONSTRAINT payments_student_id_fkey,
    ADD CONSTRAINT payments_student_id_fkey FOREIGN KEY (student_id) REFERENCES school_db.students(id);

-- В таблице groups_students уже корректные ссылки