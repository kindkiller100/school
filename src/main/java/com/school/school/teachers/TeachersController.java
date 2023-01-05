package com.school.school.teachers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/teachers")
public class TeachersController
{
        @Autowired
        private TeacherService teacherService;
        @GetMapping
        public List<Teacher> list() {
            return teacherService.list();
        }
        @PostMapping
        public void create(@RequestBody Teacher teacher){
            teacherService.create(teacher);
        }
        @DeleteMapping("/{id}")
        public void delete(@PathVariable long id) {
            teacherService.delete(id);
        }
        @PutMapping
        public void editById(@PathVariable long id, @RequestBody Teacher editTeacher) {
                teacherService.editById(id, editTeacher);
        }

}
