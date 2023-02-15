package com.school.school.lessons;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Set;

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
    @Column( name = "group_id" )
    private Long groupId;
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
        Long groupId,
        Set<Student> students
    )
    {
        this.id = id;
        this.startDateTime = startDateTime;
        this.duration = duration;
        this.subject = subject;
        this.teacher = teacher;
        this.description = description;
        this.groupId = groupId;
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


    public Long getGroupId()
    {
        return groupId;
    }


    public Set<Student> getstudents()
    {
        return students;
    }


    //переопределение метода equals(). Объекты одинаковы, если у них одинаковый id
    @Override
    public boolean equals( Object o )
    {
        if( this == o )
        {
            return true;
        }
        if( o == null || getClass() != o.getClass() )
        {
            return false;
        }
        Lesson lesson = ( Lesson ) o;
        return id == lesson.id;
    }


    //переопределение метода hashCode()
    @Override
    public int hashCode()
    {
        return Objects.hash( id );
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


    public Builder clone()
    {
        return new Lesson.Builder()
            .setId( this.id )
            .setStartDateTime( this.startDateTime )
            .setDuration( this.duration )
            .setSubject( this.subject )
            .setTeacher( this.teacher )
            .setDescription( this.description )
            .setGroupId( this.groupId )
            .setStudents( this.students );
    }


    static public class Builder
    {
        private long id;
        private LocalDateTime startDateTime;
        private short duration;
        private Subject subject;
        private Teacher teacher;
        private String description;
        private Long groupId;
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


        public Builder setGroupId( Long groupId )
        {
            this.groupId = groupId;
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
                groupId,
                students
            );
        }
    }
}
