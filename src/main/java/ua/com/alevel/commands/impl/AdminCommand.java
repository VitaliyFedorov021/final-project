package ua.com.alevel.commands.impl;

import org.apache.log4j.Logger;
import ua.com.alevel.builders.CourseBuilder;
import ua.com.alevel.builders.TeacherBuilder;
import ua.com.alevel.builders.impl.CourseBuilderImpl;
import ua.com.alevel.builders.impl.TeacherBuilderImpl;
import ua.com.alevel.commands.Command;
import ua.com.alevel.dto.Course;
import ua.com.alevel.dto.Teacher;
import ua.com.alevel.exceptions.IncorrectDataException;
import ua.com.alevel.exceptions.NoDataInDBException;
import ua.com.alevel.run.Runner;
import ua.com.alevel.services.CourseService;
import ua.com.alevel.services.StudentService;
import ua.com.alevel.services.TeacherService;
import ua.com.alevel.services.impl.CourseServiceImpl;
import ua.com.alevel.services.impl.StudentServiceImpl;
import ua.com.alevel.services.impl.TeacherServiceImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


import static ua.com.alevel.util.Reader.input;

public class AdminCommand implements Command {
    private static final Logger log = Logger.getLogger(AdminCommand.class);
    private final TeacherService teacherService = new TeacherServiceImpl();
    private final CourseService courseService = new CourseServiceImpl();
    private final StudentService studentService = new StudentServiceImpl();
    private final static String COLOR = (char) 27 + "[34m";
    @Override
    public void execute() {
        adminAction();
    }

    private void adminAction() {
        while (true) {
            int a = 0;
            try {
                a = Integer.parseInt(input("1 - Show all courses, 2 - Show all sorted courses, 3 - Show all not started courses\n " +
                        ", 4 - Show all started courses, 5 - Show all ended courses, \n" +
                        "6 - Show courses by theme, 7 - Create teacher, 8 - Create course, \n" +
                        "9 - update course, 10 - delete course, 11 - change student state, 0 - back"));
            } catch (NumberFormatException e) {
                System.out.println("Incorrect action, try again");
                adminAction();
            }
            switch (a) {
                case 1: {
                    showCourses(courseService.selectAll());
                    break;
                }
                case 2: {
                    showCourses(courseService.selectAll(true));
                    break;
                }
                case 3: {
                    showCourses(courseService.selectAllNotStarted());
                    break;
                }
                case 4: {
                    showCourses(courseService.selectAllStarted());
                    break;
                }
                case 5: {
                    showCourses(courseService.selectAllEnded());
                    break;
                }
                case 6: {
                    String theme = input("Enter the theme");
                    try {
                        showCourses(courseService.selectAllByTheme(theme));
                    } catch (NoDataInDBException | IncorrectDataException e) {
                        System.err.println(e.getLocalizedMessage());
                        adminAction();
                    }
                    break;
                }
                case 7: {
                    try {
                        teacherService.createTeacher(createTeacher());
                    } catch (NoDataInDBException | IncorrectDataException e) {
                        System.err.println("There is no course with this id or incorrect teacher name(Error message)," + e.getLocalizedMessage());
                        adminAction();
                    }
                    break;
                }
                case 8: {
                    try {
                        courseService.addCourse(createCourse());
                    } catch (IncorrectDataException e) {
                        System.err.println(e.getLocalizedMessage());
                        adminAction();
                    }
                    break;
                }
                case 9: {
                    try {
                        int id = 0;
                        System.out.println(COLOR + "Enter the id of course to update(You can see all courses)");
                        showCourses(courseService.selectAll());
                        try {
                            id = Integer.parseInt(input(null));
                        } catch (NumberFormatException e) {
                            System.out.println("Incorrect input");
                            adminAction();
                        }
                        courseService.updateCourse(createCourse(), id);
                    } catch (NoDataInDBException | IncorrectDataException e) {
                        System.out.println(e.getLocalizedMessage());
                        adminAction();
                    }
                    break;
                }
                case 10: {
                    try {
                        int id = 0;
                        System.out.println(COLOR + "Enter the id of course to delete(You can see all courses)");
                        showCourses(courseService.selectAll());
                        try {
                            id = Integer.parseInt(input(null));
                        } catch (NumberFormatException e) {
                            System.out.println("Incorrect input");
                            adminAction();
                        }
                        courseService.deleteCourse(id);
                    } catch (NoDataInDBException | IncorrectDataException e) {
                        System.out.println(e.getLocalizedMessage());
                        adminAction();
                    }
                    break;
                }
                case 11: {
                    System.out.println(COLOR + "Enter the login of student to change state(You can see all students)");
                    studentService.selectAllDistinct();
                    String login = input(null);
                    try {
                        studentService.changeState(login);
                    } catch (NoDataInDBException e) {
                        System.out.println(e.getLocalizedMessage());
                        adminAction();
                    }
                }
                case 0: {
                    new Runner().action();
                }
                default: {
                    System.err.println("Incorrect input");
                    adminAction();
                }
            }
        }
    }

    private void showCourses(List<Course> courses) {
        System.out.println("Courses: ");
        for (Course currentCourse :
                courses) {
            System.out.println((char) 27 + "[39m" + currentCourse.toString());
        }
    }

    private Teacher createTeacher() {
        TeacherBuilder teacherBuilder = new TeacherBuilderImpl();
        teacherBuilder.setName(input("Enter the teacher name"));
        System.out.println("Enter the id of course where you're teacher(You can see list of all courses)\n");
        showCourses(courseService.selectAll());
        int courseId = Integer.parseInt(input(null));
        teacherBuilder.setCourseId(courseId);
        return teacherBuilder.build();
    }

    private Course createCourse() throws IncorrectDataException {
        CourseBuilder courseBuilder = new CourseBuilderImpl();
        courseBuilder.setName(input("Enter the course name"));
        courseBuilder.setTheme(input("Enter the course theme"));
        System.out.println("Enter the start date in format [yyyy-MM-dd]");
        Date startDate = getDate();
        System.out.println("Enter the end date in format [yyyy-MM-dd]");
        Date endDate = getDate();
        if (startDate.getTime() > endDate.getTime()) {
            throw new IncorrectDataException("Incorrect date");
        }
        courseBuilder.setStartDate(startDate);
        courseBuilder.setEndDate(endDate);
        return courseBuilder.build();
    }

    private static Date getDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        while (true) {
            try {
                final String DateLine = input(null);
                final Date date = format.parse(DateLine);
                return date;
            } catch (ParseException e) {
                log.error("Can't parse date " + e);
                System.out.println("Try again");
            }
        }
    }

}
