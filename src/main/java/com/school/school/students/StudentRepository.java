package com.school.school.students;

import com.school.school.CustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


public interface StudentRepository extends JpaRepository<Student, Long>, CustomRepository<Student> {
    List<Student> findAllByDeletedIsTrue();

    List<Student> findAllByDeletedIsFalse();
    @Query(value = "SELECT * FROM students " +
            "WHERE name ILIKE %?1% " +
            "OR secondname ILIKE %?1% " +
            "OR lastname ILIKE %?1% " +
            "OR telephonenumber ILIKE %?1%"
            , nativeQuery = true)
    List<Student> findAllByFilter(String like);

    @Query(value ="SELECT * FROM students WHERE dateofbirth >= ?1 AND dateofbirth <= ?2", nativeQuery = true)
    List<Student> findAllByDateOfBirthRange(LocalDate fromDate, LocalDate uptoDate);


}
