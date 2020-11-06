package ua.com.alevel.commands.impl;

import org.apache.log4j.Logger;
import ua.com.alevel.builders.StudentBuilder;
import ua.com.alevel.builders.impl.StudentBuilderImpl;
import ua.com.alevel.commands.Command;
import ua.com.alevel.dto.Course;
import ua.com.alevel.dto.Student;
import ua.com.alevel.exceptions.IncorrectDataException;
import ua.com.alevel.exceptions.NoDataInDBException;
import ua.com.alevel.run.Runner;
import ua.com.alevel.services.CourseService;
import ua.com.alevel.services.StudentService;
import ua.com.alevel.services.impl.CourseServiceImpl;
import ua.com.alevel.services.impl.StudentServiceImpl;

import java.util.ArrayList;
import java.util.List;
import static ua.com.alevel.util.Reader.input;

public class StudentCommand implements Command {
    private final Logger log = Logger.getLogger(StudentCommand.class);
    private final StudentService studentService = new StudentServiceImpl();
    private final CourseService courseService = new CourseServiceImpl();
    private static final String COLOR = (char) 27 + "[34m";

    @Override
    public void execute() {
        studentAction();
    }

    private void studentAction() {
        while (true) {
            int a = 0;
            try {
                a = Integer.parseInt(input("1 - create account, 2 - log in, 0 - back"));
            } catch (NumberFormatException e) {
                System.err.println("Incorrect action, try again");
                studentAction();
            }
            switch (a) {
                case 1: {
                    try {
                        studentService.createStudent(createStudent());
                    } catch (IncorrectDataException | NoDataInDBException e) {
                        System.err.println("Can't create student, check is your input is correct?(Error message): "
                                + e.getLocalizedMessage());
                        continue;
                    }
                    break;
                }
                case 2: {
                    String login = enterLogin();
                    processCreatedStudent(login);
                    break;
                }
                case 0: {
                    new Runner().action();
                    break;
                }
                default: {
                    System.err.println("Incorrect input");
                    break;
                }
            }
        }
    }

    private String enterLogin() {
        String login = input("Enter the login");
        Student student = null;
        try {
            student = studentService.findStudentByLogin(login);
        } catch (IncorrectDataException | NoDataInDBException e) {
            System.err.println(COLOR + "Try again (Error message):" + e.getLocalizedMessage() + " 0 - back, 1 - enter login");
            int a = 0;
            try {
                a = Integer.parseInt(input(null));
            } catch (NumberFormatException ex) {
                System.err.println("Incorrect input, go back");
                studentAction();
            }
            if (a == 0) {
                studentAction();
            }
            enterLogin();
        }
        return login;
    }

    private void processCreatedStudent(String login) {
        while (true) {
            int a = 0;
            try {
                a = Integer.parseInt(input("1 - register to new course, 2 - show not started courses, " +
                        "3 - show started courses, 4 - show ended courses, 5 - show all student's courses, 0 - back"));
            } catch (NumberFormatException e) {
                System.err.println("Incorrect action, try again");
                processCreatedStudent(login);
            }
            switch (a) {
                case 1: {
                    Student student = null;
                    try {
                        student = studentService.findStudentByLogin(login);
                        if (student.getState().equalsIgnoreCase("blocked")) {
                            System.err.println("You have been blocked, you can't register to new course");
                            studentAction();
                        }
                    } catch (IncorrectDataException | NoDataInDBException e) {
                        System.err.println("Login is incorrect or there is no this login in DB, " + e.getLocalizedMessage());
                        studentAction();
                    }
                    try {
                        System.out.println(COLOR + "Enter the id of course where you want to attend(You can see list of all courses)\n");
                        showCourses(courseService.selectAll());
                        int courseId = Integer.parseInt(input(null));
                        studentService.registerToCourse(student, courseId);
                    } catch (IncorrectDataException e) {
                        System.err.println("Login is incorrect or student registred for this course," + e.getLocalizedMessage());
                        processCreatedStudent(login);
                    }
                    break;
                }
                case 2: {
                    List<Course> courses = new ArrayList<>();
                    try {
                        courses = courseService.selectAllNotStarted(login);
                    } catch (IncorrectDataException e) {
                        System.err.println("Login is incorrect or there is no this login in DB, " + e.getLocalizedMessage());
                        studentAction();
                    }
                    System.out.println(COLOR + "Not started courses:");
                    showCourses(courses);
                    break;
                }
                case 3: {
                    List<Course> courses = new ArrayList<>();
                    try {
                        courses = courseService.selectAllStarted(login);
                    } catch (IncorrectDataException e) {
                        System.err.println("Login is incorrect or there is no this login in DB, " + e.getLocalizedMessage());
                        studentAction();
                    }
                    System.out.println(COLOR +"Started courses:");
                    showCourses(courses);
                    break;
                }
                case 4: {
                    List<Course> courses = new ArrayList<>();
                    try {
                        courses = courseService.selectAllEnded(login);
                    } catch (IncorrectDataException e) {
                        System.err.println("Login is incorrect or there is no this login in DB, " + e.getLocalizedMessage());
                        studentAction();
                    }
                    System.out.println(COLOR + "Ended courses: ");
                    showCourses(courses);
                    break;
                }
                case 5: {
                    List<Course> courses = new ArrayList<>();
                    courses = courseService.selectAll(login, true);
                    System.out.println(COLOR + "All courses: ");
                    showCourses(courses);
                    break;
                }
                case 0: {
                    studentAction();
                }
                default: {
                    System.err.println("Incorrect input ");
                    processCreatedStudent(login);
                }
            }
        }
    }

    private Student createStudent() throws IncorrectDataException {
        StudentBuilder builder = new StudentBuilderImpl();
        String name = input("Enter the student name"),
                state = "unblocked",
                surname = input("Enter the student surname"),
                login = input("Enter the student login");
        System.out.println(COLOR + "Enter the id of course where you want to attend(You can see list of all courses)\n");
        showCourses(courseService.selectAll());
        int courseId = Integer.parseInt(input(null));
        builder.setState(state);
        builder.setSurname(surname);
        builder.setCourseId(courseId);
        builder.setName(name);
        builder.setLogin(login);
        return builder.build();
    }

    private void showCourses(List<Course> courses) {
        for (Course currentCourse :
                courses) {
            System.out.println((char) 27 + "[39m" + currentCourse.toString());
        }
    }

}
