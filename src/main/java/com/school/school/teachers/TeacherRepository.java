package com.school.school.teachers;

import com.school.school.CustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long>,CustomRepository<Teacher>
{
    Page<Teacher> findAllByDeletedIsTrue(Pageable pageable);
    Page<Teacher> findAllByDeletedIsFalse(Pageable pageable);
    @Query(value = "SELECT * FROM school_db.teachers " +
        "WHERE name ILIKE %?1% " +
        "OR secondname ILIKE %?1% " +
        "OR lastname ILIKE %?1% " +
        "OR telephonenumber ILIKE %?1%"
        , nativeQuery = true)
    Page<Teacher> findAllByFilter(String like, Pageable pageable);
}