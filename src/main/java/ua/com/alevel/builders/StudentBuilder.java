package ua.com.alevel.builders;

import ua.com.alevel.dto.Student;

public interface StudentBuilder {
    StudentBuilder setCourseId(int courseId);

    StudentBuilder setId(int id);

    StudentBuilder setName(String name);

    StudentBuilder setSurname(String surname);

    StudentBuilder setLogin(String login);

    StudentBuilder setState(String state);

    Student build();
}
