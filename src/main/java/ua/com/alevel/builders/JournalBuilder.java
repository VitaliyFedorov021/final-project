package ua.com.alevel.builders;

import ua.com.alevel.dto.Journal;

public interface JournalBuilder {
    JournalBuilder setCourseId(int courseId);

    JournalBuilder setTeacherId(int teacherId);

    JournalBuilder setStudentId(int studentId);

    JournalBuilder setMark(int mark);

    Journal build();
}
