package com.school.school.students_groups;

import com.school.school.CustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentsGroupRepository extends JpaRepository<StudentsGroup, Long>, CustomRepository<StudentsGroup> {
    //возвращает количество студентов с данным id в списках групп
    //для проверки наличия студента в какой-либо из групп
    @Query(value = "SELECT COUNT(*) FROM school_db.students_groups_students " +
            "WHERE student_id = ?1"
            , nativeQuery = true)
    int countStudentIdInGroups(long id);
}
