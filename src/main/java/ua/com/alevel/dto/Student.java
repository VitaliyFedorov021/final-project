package ua.com.alevel.dto;

public class Student {
    private final int courseId;
    private final String name, surname, state, login;

    public Student(int courseId, String name, String surname, String state, String login) {
        this.courseId = courseId;
        this.name = name;
        this.surname = surname;
        this.state = state;
        this.login = login;
    }

    public int getCourseId() {
        return courseId;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    @Override
    public String toString() {
        return "Student{" +
                "courseId=" + courseId +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", state='" + state + '\'' +
                ", login='" + login + '\'' +
                '}';
    }

    public String getState() {
        return state;
    }

    public String getLogin() {
        return login;
    }
}
