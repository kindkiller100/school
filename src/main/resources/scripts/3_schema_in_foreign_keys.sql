ALTER TABLE school_db.lessons DROP CONSTRAINT lessons_subject_id_fkey;
ALTER TABLE school_db.lessons ADD CONSTRAINT lessons_subject_id_fkey FOREIGN KEY (subject_id) REFERENCES subjects (id) NOT NULL;