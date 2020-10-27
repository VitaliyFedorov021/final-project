package ua.com.alevel.builders;

import ua.com.alevel.dto.Course;
import ua.com.alevel.dto.Theme;
import java.util.Date;

public interface CourseBuilder {
    CourseBuilder setId(int id);

    CourseBuilder setName(String name);

    CourseBuilder setTheme(String theme);

    CourseBuilder setStartDate(Date date);

    CourseBuilder setEndDate(Date date);

    Course build();
}
