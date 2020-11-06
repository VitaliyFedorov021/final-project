package ua.com.alevel.commands.impl;

import ua.com.alevel.builders.JournalBuilder;
import ua.com.alevel.builders.impl.JournalBuilderImpl;
import ua.com.alevel.commands.Command;
import ua.com.alevel.dto.Course;
import ua.com.alevel.dto.Journal;
import ua.com.alevel.dto.Student;
import ua.com.alevel.dto.Teacher;
import ua.com.alevel.exceptions.IncorrectDataException;
import ua.com.alevel.exceptions.NoDataInDBException;
import ua.com.alevel.services.CourseService;
import ua.com.alevel.services.JournalService;
import ua.com.alevel.services.StudentService;
import ua.com.alevel.services.TeacherService;
import ua.com.alevel.services.impl.CourseServiceImpl;
import ua.com.alevel.services.impl.JournalServiceImpl;
import ua.com.alevel.services.impl.StudentServiceImpl;
import ua.com.alevel.services.impl.TeacherServiceImpl;

import java.util.List;

import static ua.com.alevel.util.Reader.input;

public class TeacherCommand implements Command {
    private final JournalService journalService = new JournalServiceImpl();
    private static final String COLOR = (char) 27 + "[34m";
    private final CourseService courseService = new CourseServiceImpl();
    private final StudentService studentService = new StudentServiceImpl();
    private final TeacherService teacherService = new TeacherServiceImpl();

    @Override
    public void execute() {
        teacherAction();
    }

    private void teacherAction() {
        System.out.println(COLOR + "Enter your teacher id(you can see all teachers)");
        showTeachers(teacherService.selectAll());
        int teacherId = 0;
        try {
            teacherId = Integer.parseInt(input(null));
            teacherService.findTeacherById(teacherId);
        } catch (NumberFormatException | NoDataInDBException | IncorrectDataException e) {
            System.out.println("Incorrect input, " + e.getLocalizedMessage());
            teacherAction();
        }
        while (true) {
            int a = 0;
            try {
                a = Integer.parseInt(input("1 - show journal info, 2 - show journal info by course id," +
                        " 3 - show journal info by student login, 4 - create new record journal, " +
                        "5 - update record"));
            } catch (NumberFormatException e) {
                System.err.println("Incorrect action, try again");
                teacherAction();
            }
            switch (a) {
                case 1: {
                   showEntries(journalService.selectAllEntries());
                   break;
                }
                case 2: {
                    int id = 0;
                    try {
                        id = Integer.parseInt(input("Enter course id"));
                    } catch (NumberFormatException e) {
                        System.out.println("Incorrect input");
                        teacherAction();
                    }
                    try {
                        showEntries(journalService.selectAllByCourseId(id));
                    } catch (IncorrectDataException | NoDataInDBException e) {
                        System.out.println(e.getLocalizedMessage());
                        teacherAction();
                    }
                    break;
                }
                case 3: {
                    String login = input("Enter the login");
                    try {
                        showEntries(journalService.selectAllByStudentLogin(login));
                    } catch (IncorrectDataException | NoDataInDBException e) {
                        System.out.println(e.getLocalizedMessage());
                        teacherAction();
                    }
                    break;
                }
                case 4: {
                    try {
                        journalService.createNewEntry(createJournalEntry(teacherId, courseService.selectCourseByTeacherId(teacherId).getId()));
                    } catch (NoDataInDBException | IncorrectDataException e) {
                        System.out.println(e.getLocalizedMessage());
                    }
                    break;
                }
                case 5: {
                    try {
                        journalService.updateJournalEntry(createJournalEntry(teacherId, courseService.selectCourseByTeacherId(teacherId).getId()));
                    } catch (NoDataInDBException | IncorrectDataException e) {
                        System.out.println(e.getLocalizedMessage());
                    }
                    break;
                }
            }
        }

    }

    private void showEntries(List<Journal> journals) {
        for (Journal currentJournal :
                journals) {
            System.out.println(currentJournal.toString());
        }
    }

    private void showCourses(List<Course> courses) {
        System.out.println("Courses: ");
        for (Course currentCourse :
                courses) {
            System.out.println((char) 27 + "[39m" + currentCourse.toString());
        }
    }

    private void showTeachers(List<Teacher> teachers) {
        for (Teacher currentTeacher:
                teachers) {
            System.out.println((char) 27 + "[39m" + currentTeacher.toString());
        }
    }

    private Journal createJournalEntry(int teacherId, int courseId) throws IncorrectDataException, NoDataInDBException {
        JournalBuilder journalBuilder = new JournalBuilderImpl();
        journalBuilder.setCourseId(courseId);
        System.out.println(COLOR + "Enter the student id(you can see all students in this course)");
        studentService.selectAllDistinctByCourseId(courseId);
        String login = input(null);
        studentService.findStudentByLogin(login);
        int id = studentService.findId(login, courseId);
        journalBuilder.setStudentId(id);
        journalBuilder.setTeacherId(teacherId);
        journalBuilder.setMark(Integer.parseInt(input("Enter the student mark(from 1 to 100)")));
        return journalBuilder.build();
    }

}
