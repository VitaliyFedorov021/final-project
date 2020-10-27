package ua.com.alevel.builders.impl;

import ua.com.alevel.builders.JournalBuilder;
import ua.com.alevel.dto.Journal;

public class JournalBuilderImpl implements JournalBuilder {
    private int courseId, teacherId, studentId, mark;

    @Override
    public JournalBuilder setCourseId(int courseId) {
        this.courseId = courseId;
        return this;
    }

    @Override
    public JournalBuilder setTeacherId(int teacherId) {
        this.teacherId = teacherId;
        return this;
    }

    @Override
    public JournalBuilder setStudentId(int studentId) {
        this.studentId = studentId;
        return this;
    }

    @Override
    public JournalBuilder setMark(int mark) {
        this.mark = mark;
        return this;
    }


    @Override
    public Journal build() {
        return new Journal(courseId, teacherId, studentId, mark);
    }
}
