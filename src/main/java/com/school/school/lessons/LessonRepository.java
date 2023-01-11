package com.school.school.lessons;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findLessonsByStartDateTimeBetween(LocalDateTime dateFrom, LocalDateTime dateTo);

    List<Lesson> findLessonsByTeacherId(long id);

    @Query(value = "select * from school_db.lessons where school_db.lessons.id in " +
            "(select lesson_id from school_db.student_lesson where school_db.student_lesson.student_id = ?1)", nativeQuery = true)
    List<Lesson> findLessonsByStudentId(long id);

    @Query(value = "select sum(duration) from school_db.lessons where school_db.lessons.teacher_id = ?1" +
            " and school_db.lessons.startdatetime between ?2 and ?3", nativeQuery = true)
    long countHoursOfLessonsByTeacherInRange(long id, LocalDateTime dateFrom, LocalDateTime dateTo);

    @Query(value = "select sum(duration) from school_db.lessons where school_db.lessons.id in " +
            "(select lesson_id from school_db.student_lesson where school_db.student_lesson.student_id = ?1) and " +
            "school_db.lessons.startdatetime between ?2 and ?3", nativeQuery = true)
    long findDurationByStudentIdInRange(long id, LocalDateTime dateFrom, LocalDateTime dateTo);
}
