ALTER TABLE school_db.groups RENAME TO students_groups;
ALTER TABLE school_db.groups_students RENAME TO students_groups_students;
ALTER TABLE school_db.students_groups_students
  DROP CONSTRAINT groups_students_group_id_fkey,
  ADD CONSTRAINT groups_students_group_id_fkey FOREIGN KEY (group_id)
  REFERENCES school_db.students_groups(id) ON DELETE CASCADE;