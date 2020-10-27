package ua.com.alevel.builders.impl;

import ua.com.alevel.builders.TeacherBuilder;
import ua.com.alevel.dto.Teacher;

public class TeacherBuilderImpl implements TeacherBuilder {
    private int id, courseId;
    private String name;

    @Override
    public TeacherBuilder setId(int id) {
        this.id = id;
        return this;
    }

    @Override
    public TeacherBuilder setCourseId(int courseId) {
        this.courseId = courseId;
        return this;
    }

    @Override
    public TeacherBuilder setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public Teacher build() {
        return new Teacher(id, courseId, name);
    }
}
