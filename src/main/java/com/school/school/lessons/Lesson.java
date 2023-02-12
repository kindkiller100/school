package com.school.school.lessons;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.school.school.lessons_groups.LessonsGroups;
import com.school.school.subjects.Subject;
import com.school.school.teachers.Teacher;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.school.school.students.Student;

@Entity
@Table( name = "lessons", schema = "school_db" )
public class Lesson
{
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private long id;                    //id занятия
    @NotNull( message = "Необходимо указать начало занятия." )
    @Column( name = "startdatetime" )
    private LocalDateTime startDateTime;//дата и время начала занятия
    @Min( value = 30, message = "Продолжительность занятия должна быть в пределах от 30 до 210 минут" )
    @Max( value = 210, message = "Продолжительность занятия должна быть в пределах от 30 до 210 минут" )
    private short duration;             //продолжительность занятия в минутах
    @NotNull
    @ManyToOne( fetch = FetchType.EAGER )
    private Subject subject;            //предмет
    @NotNull
    @ManyToOne( fetch = FetchType.EAGER )
    private Teacher teacher;            //преподаватель
    @Size( max = 40, message = "Размер описания не должен превышать 40 символов." )
    private String description;         //расшифровка (подробное описание) занятия
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private LessonsGroups group;
    @ManyToMany( cascade = { CascadeType.PERSIST } )
    @JoinTable(
        name = "student_lesson",
        schema = "school_db",
        joinColumns = { @JoinColumn( name = "lesson_id" ) },
        inverseJoinColumns = { @JoinColumn( name = "student_id" ) }
    )
    private Set<Student> students;


    //конструктор без параметров
    protected Lesson()
    {
    }


    //конструктор со всеми параметрами
    private Lesson(
        long id,
        LocalDateTime startDateTime,
        short duration,
        Subject subject,
        Teacher teacher,
        String description,
        LessonsGroups group,
        Set<Student> students
    )
    {
        this.id = id;
        this.startDateTime = startDateTime;
        this.duration = duration;
        this.subject = subject;
        this.teacher = teacher;
        this.description = description;
        this.group = group;
        this.students = students;
    }


    public long getId()
    {
        return id;
    }


    public LocalDateTime getStartDateTime()
    {
        return startDateTime;
    }


    public short getDuration()
    {
        return duration;
    }


    public Subject getSubject()
    {
        return subject;
    }


    public Teacher getTeacher()
    {
        return teacher;
    }


    public String getDescription()
    {
        return description;
    }


    public LessonsGroups getGroup() {
        return group;
    }


    public Set<Student> getStudents()
    {
        return students;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public void setDuration(short duration) {
        this.duration = duration;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    //переопределение метода equals(). Объекты одинаковы, если у них одинаковый id, id преподавателя, id предмета и дата начала занятия
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lesson lesson = (Lesson) o;
        return (id == lesson.id) &&
                ((teacher != null ? teacher.getId() : 0) == (lesson.teacher != null ? lesson.teacher.getId() : 0)) &&
                ((subject != null ? subject.getId() : 0) == (lesson.subject != null ? lesson.subject.getId() : 0)) &&
                (Objects.equals(startDateTime, lesson.getStartDateTime()));
    }


    //переопределение метода hashCode()
    @Override
    public int hashCode() {
        return Objects.hash(id,
                teacher != null ? teacher.getId() : 0,
                subject != null ? subject.getId() : 0,
                startDateTime);
    }


    //переопределение метода toString()
    @Override
    public String toString()
    {
        return "Lesson{" +
            "subject: «" + subject.getTitle() +
            "», teacher: " + teacher.getFullName() +
            ", date of lesson: " + startDateTime.toLocalDate() +        //получаем дату занятия
            ", start at: " + startDateTime.toLocalTime().format( DateTimeFormatter.ofPattern( "HH:mm" ) ) +     //получаем время занятия в формате ЧЧ.ММ
            ", end at: " + startDateTime.toLocalTime().plusMinutes( duration ).format( DateTimeFormatter.ofPattern( "HH:mm" ) ) + //получаем время занятия, добавляем продолжительность занятия и форматируем результат
            " (duration " + duration + " min.)" +
            ", description: '" + description + '\'' +
            '}';
    }

    //метод пирнимает объекты предмета, преподавателя и список студентов и изменяет соответствующие поля объекта Lesson
    public void setIfNotEquals(Subject subject, Teacher teacher, Set<Student> students) {
        if ((subject != null ? subject.getId() : 0) != (this.subject != null ? this.subject.getId() : 0)) {
            this.subject = subject;
        }
        if ((teacher != null ? teacher.getId() : 0) != (this.teacher != null ? this.teacher.getId() : 0)) {
            this.teacher = teacher;
        }
        if (!Objects.equals(students, this.students)) {
            changeStudents(students);
        }
    }

    //метод изменяет студентов объекта Lesson на студентов из принимаемого параметра
    private void changeStudents(Set<Student> newStudents) {
        //очищаем старый список
        this.students.clear();
        //добавляем новый
        this.students.addAll(newStudents);
    }

    //метод проверяет эквивалентность полей Lesson с параметрами
    public boolean checkEqualsFields(long subjectId, long teacherId, Set<Long> studentsId) {
        return (subject != null ? subject.getId() : 0) == subjectId &&
                (teacher != null ? teacher.getId() : 0) == teacherId &&
                Objects.equals(students != null ? students.stream().map(Student::getId).collect(Collectors.toSet()) : null, studentsId);
    }

    public Builder clone()
    {
        return new Lesson.Builder()
            .setId( this.id )
            .setStartDateTime( this.startDateTime )
            .setDuration( this.duration )
            .setSubject( this.subject )
            .setTeacher( this.teacher )
            .setDescription( this.description )
            .setGroup( this.group )
            .setStudents( this.students );
    }

    public static Builder builder() {
        return new Builder();
    }

    static public class Builder
    {
        private long id;
        private LocalDateTime startDateTime;
        private short duration;
        private Subject subject;
        private Teacher teacher;
        private String description;
        private LessonsGroups group;
        private Set<Student> students;


        public Builder setId( long id )
        {
            this.id = id;
            return this;
        }


        public Builder setStartDateTime( LocalDateTime startDateTime )
        {
            this.startDateTime = startDateTime;
            return this;
        }


        public Builder setDuration( short duration )
        {
            this.duration = duration;
            return this;
        }


        public Builder setSubject( Subject subject )
        {
            this.subject = subject;
            return this;
        }


        public Builder setTeacher( Teacher teacher )
        {
            this.teacher = teacher;
            return this;
        }


        public Builder setDescription( String description )
        {
            this.description = description;
            return this;
        }


        public Builder setGroup( LessonsGroups group )
        {
            this.group = group;
            return this;
        }


        public Builder setStudents( Set<Student> students )
        {
            this.students = students;
            return this;
        }


        public Lesson build()
        { //возвращает объект внешнего класса с заданными параметрами
            return new Lesson(
                id,
                startDateTime,
                duration,
                subject,
                teacher,
                description,
                group,
                students
            );
        }
    }
}
