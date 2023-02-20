package com.school.school.students;

import com.school.school.CustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;


@Repository
public interface StudentRepository extends JpaRepository<Student, Long>, CustomRepository<Student> {

    Page<Student> findAllByDeletedIsTrue(Pageable pageable);

    Page<Student> findAllByDeletedIsFalse(Pageable pageable);

    @Query(value = "SELECT * FROM school_db.students " +
            "WHERE name ILIKE %?1% " +
            "OR secondname ILIKE %?1% " +
            "OR lastname ILIKE %?1% " +
            "OR telephonenumber ILIKE %?1%"
            , nativeQuery = true)
    Page<Student> findAllByFilter(String like, Pageable pageable);

    @Query(value = "SELECT * FROM school_db.students WHERE dateofbirth >= ?1 AND dateofbirth <= ?2", nativeQuery = true)
    Page<Student> findAllByDateOfBirthRange(LocalDate fromDate, LocalDate uptoDate, Pageable pageable);

    @Query(value = "SELECT * FROM school_db.students WHERE id IN " +
            "(SELECT student_id FROM school_db.students_groups_students WHERE group_id = ?1)"
            , nativeQuery = true)
    Page<Student> getAllStudentsInGroup(long id, Pageable pageable);

    Set<Student> getAllByIdIn(Collection<Long> findStudents);
}
