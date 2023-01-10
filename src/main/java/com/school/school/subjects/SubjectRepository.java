package com.school.school.subjects;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    List<Subject> findAllByDeletedIsTrue();
    List<Subject> findAllByDeletedIsFalse();
    boolean existsByTitle(String title);
    boolean existsByTitleAndIdNotLike(String title, long id);
}
