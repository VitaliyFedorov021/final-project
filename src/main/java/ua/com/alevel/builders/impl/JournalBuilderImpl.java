package ua.com.alevel.builders.impl;

import ua.com.alevel.builders.JournalBuilder;
import ua.com.alevel.dto.Journal;

public class JournalBuilderImpl implements JournalBuilder {
    private int courseId, teacherId, mark;
    private int studentId;
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
    public JournalBuilder setStudentId(int id) {
        this.studentId = id;
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
