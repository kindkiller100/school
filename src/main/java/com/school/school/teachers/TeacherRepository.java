package com.school.school.teachers;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher,Long>
{
    List<Teacher> findAllByDeletedIsTrue();
    List<Teacher> findAllByDeletedIsFalse();
    @Query(value = "SELECT * FROM school_db.teachers " +
            "WHERE name ILIKE %?1% " +
            "OR secondname ILIKE %?1% " +
            "OR lastname ILIKE %?1% " +
            "OR telephonenumber ILIKE %?1%"
            , nativeQuery = true)
    List<Teacher> findAllByFilter(String like);
}