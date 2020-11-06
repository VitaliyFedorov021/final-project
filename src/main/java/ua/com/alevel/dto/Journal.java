package ua.com.alevel.dto;

public class Journal {
    private final int courseId, teacherId, mark;
    private final int studentId;

    public int getCourseId() {
        return courseId;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public int getMark() {
        return mark;
    }

    public int getStudentId() {
        return studentId;
    }

    @Override
    public String toString() {
        return "Journal{" +
                "courseId=" + courseId +
                ", teacherId=" + teacherId +
                ", mark=" + mark +
                ", studentId=" + studentId +
                '}';
    }

    public Journal(int courseId, int teacherId, int studentId, int mark) {
        this.courseId = courseId;
        this.teacherId = teacherId;
        this.mark = mark;
        this.studentId = studentId;
    }
}
