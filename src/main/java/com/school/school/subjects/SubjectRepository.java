package com.school.school.subjects;

import com.school.school.CustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long>, CustomRepository<Subject> {
    Page<Subject> findAllByDeletedIsTrue(Pageable pageable);
    Page<Subject> findAllByDeletedIsFalse(Pageable pageable);
    boolean existsByTitle(String title);
    boolean existsByTitleAndIdNot(String title, long id);
}
