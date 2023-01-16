-- удаляем старую и создаем новую схему
drop schema if exists school_db cascade;
create schema school_db;

-- создание таблиц
CREATE TABLE school_db.students
(
    id serial PRIMARY KEY,
    name varchar(20) NOT NULL,
    secondName varchar(20),
    lastName varchar(20) NOT NULL,
    dateOfBirth date,
    gender varchar(20),
    telephoneNumber varchar(20) NOT NULL,
    info varchar(200),
    deleted boolean DEFAULT FALSE
);

CREATE TABLE school_db.teachers
(
    id serial PRIMARY KEY,
    name varchar(20) NOT NULL,
    secondName varchar(20),
    lastName varchar(20) NOT NULL,
    dateOfBirth date,
    gender varchar(20),
    telephoneNumber varchar(20) NOT NULL,
    info varchar(200),
    deleted boolean DEFAULT FALSE
);

CREATE TABLE school_db.subjects
(
    id serial PRIMARY KEY,
    title varchar(20) NOT NULL,
    description varchar(140),
    deleted boolean DEFAULT FALSE
);

CREATE TABLE school_db.lessons
(
    id serial PRIMARY KEY,
    startDateTime timestamp NOT NULL,
    duration smallint CHECK (duration > 0),
    subject_id int NOT NULL REFERENCES subjects(id),
    teacher_id int NOT NULL REFERENCES teachers(id),
    description VARCHAR (40)
);

CREATE TABLE school_db.student_lesson
(
    id serial PRIMARY KEY,
    student_id int NOT NULL REFERENCES students(id),
    lesson_id int NOT NULL REFERENCES lessons(id)
);

CREATE TABLE school_db.payments
(
    id serial PRIMARY KEY,
    date_time timestamp NOT NULL,
    sum money NOT NULL,
    student_id int REFERENCES students(id),
    auto_identified boolean DEFAULT NULL,
    info varchar(200)
);
