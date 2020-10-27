package ua.com.alevel.dto;

public class Teacher {
    private final int id, courseId;
    private final String name;

    public Teacher(int id, int courseId, String name) {
        this.id = id;
        this.courseId = courseId;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public int getCourseId() {
        return courseId;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + id +
                ", courseId=" + courseId +
                ", name='" + name + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }
}
