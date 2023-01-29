package com.school.school.lessons_groups;

import com.school.school.CustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonsGroupsRepository extends JpaRepository<LessonsGroups, Long>, CustomRepository<LessonsGroups> {
    boolean existsByTitle(String title);
    boolean existsByTitleAndIdNot(String title, long id);
}
