package com.school.school.students_groups;

import com.school.school.CustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentsGroupRepository extends JpaRepository<StudentsGroup, Long>, CustomRepository<StudentsGroup> {

}
