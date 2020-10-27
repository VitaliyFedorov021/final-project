package ua.com.alevel.dto;

public class Journal {
    private final int courseId, teacherId, studentId, mark;

    public Journal(int courseId, int teacherId, int studentId, int mark) {
        this.courseId = courseId;
        this.teacherId = teacherId;
        this.studentId = studentId;
        this.mark = mark;
    }

    public int getCourseId() {
        return courseId;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public int getStudentId() {
        return studentId;
    }

    public int getMark() {
        return mark;
    }

    @Override
    public String toString() {
        return "Journal{" +
                "courseId=" + courseId +
                ", teacherId=" + teacherId +
                ", studentId=" + studentId +
                ", mark=" + mark +
                '}';
    }
}
