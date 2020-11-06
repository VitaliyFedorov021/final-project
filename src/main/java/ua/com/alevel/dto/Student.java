package ua.com.alevel.dto;

public class Student {
    private final int courseId, id;
    private final String name, surname, state, login;

    public Student(int courseId, int id, String name, String surname, String state, String login) {
        this.courseId = courseId;
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.state = state;
        this.login = login;
    }

    public int getCourseId() {
        return courseId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getState() {
        return state;
    }

    public String getLogin() {
        return login;
    }
}
