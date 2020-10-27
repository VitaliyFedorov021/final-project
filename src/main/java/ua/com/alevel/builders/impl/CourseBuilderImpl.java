package ua.com.alevel.builders.impl;

import ua.com.alevel.builders.CourseBuilder;
import ua.com.alevel.dto.Course;
import ua.com.alevel.dto.Theme;

import java.util.Date;

public class CourseBuilderImpl implements CourseBuilder {
    private int id;
    private String name, theme;
    private Date startDate, endDate;

    @Override
    public CourseBuilder setId(int id) {
        this.id = id;
        return this;
    }

    @Override
    public CourseBuilder setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public CourseBuilder setTheme(String theme) {
        this.theme = theme;
        return this;
    }

    @Override
    public CourseBuilder setStartDate(Date date) {
        this.startDate = date;
        return this;
    }

    @Override
    public CourseBuilder setEndDate(Date date) {
        this.endDate = date;
        return this;
    }

    @Override
    public Course build() {
       return new Course(id, name, theme, startDate, endDate);
    }
}
