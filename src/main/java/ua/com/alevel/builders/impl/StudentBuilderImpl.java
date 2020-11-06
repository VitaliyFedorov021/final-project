package ua.com.alevel.builders.impl;

import ua.com.alevel.builders.StudentBuilder;
import ua.com.alevel.dto.Student;

public class StudentBuilderImpl implements StudentBuilder {
    private int courseId, id;
    private String name, surname, state, login;

    @Override
    public StudentBuilder setId(int id) {
        this.id = id;
        return this;
    }

    @Override
    public StudentBuilder setCourseId(int courseId) {
        this.courseId = courseId;
        return this;
    }

    @Override
    public StudentBuilder setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public StudentBuilder setSurname(String surname) {
        this.surname = surname;
        return this;
    }

    @Override
    public StudentBuilder setLogin(String login) {
        this.login = login;
        return this;
    }

    @Override
    public StudentBuilder setState(String state) {
        this.state = state;
        return this;
    }

    @Override
    public Student build() {
        return new Student(courseId, id, name, surname, state, login);
    }


}
