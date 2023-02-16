package com.school.school.lessons;

import com.school.school.CustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long>, CustomRepository<Lesson> {
    Page<Lesson> findLessonsByStartDateTimeBetween(LocalDateTime dateFrom, LocalDateTime dateTo, Pageable pageable);

    Page<Lesson> findLessonsByTeacherId(long id, Pageable pageable);
    boolean existsBySubjectId(long id);
    boolean existsByTeacherId(long id);

    @Query(value = "select * from school_db.lessons where school_db.lessons.id in " +
        "(select lesson_id from school_db.student_lesson where school_db.student_lesson.student_id = ?1)", nativeQuery = true)
    Page<Lesson> findLessonsByStudentId(long id, Pageable pageable);

    @Query(value = "select sum(duration) from school_db.lessons where school_db.lessons.teacher_id = ?1" +
        " and school_db.lessons.startdatetime between ?2 and ?3", nativeQuery = true)
    long countDurationOfLessonsByTeacherInRange(long id, LocalDateTime dateFrom, LocalDateTime dateTo);

    @Query(value = "select sum(duration) from school_db.lessons where " +
        "school_db.lessons.startdatetime between ?1 and ?2 and " +
        "school_db.lessons.id in (select lesson_id from school_db.student_lesson where school_db.student_lesson.student_id = ?3)"
        , nativeQuery = true)
    long findDurationByStudentIdInRange(LocalDateTime dateFrom, LocalDateTime dateTo, long id);
    //возвращает количество студентов с данным id, связанных с какими-либо занятиями
    @Query(value = "SELECT COUNT(*) FROM school_db.student_lesson " +
            "WHERE student_id = ?1", nativeQuery = true)
    int countStudentIdInLessons(long id);

    List<Lesson> findLessonByGroupIdAndStartDateTimeBefore(long groupId, LocalDateTime dateTime);

    @Transactional
    @Modifying(clearAutomatically = true)
    void deleteAllByGroupIdAndStartDateTimeAfter(long groupId, LocalDateTime startDateTime);
}
