package ua.com.alevel.builders;


import ua.com.alevel.dto.Teacher;

public interface TeacherBuilder {
    TeacherBuilder setId(int id);

    TeacherBuilder setCourseId(int courseId);

    TeacherBuilder setName(String name);

    Teacher build();
}
