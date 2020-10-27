package ua.com.alevel;

import org.apache.log4j.Logger;
import ua.com.alevel.dao.impl.StudentDaoImpl;
import ua.com.alevel.dto.Student;

import java.util.List;


public class Main {
    public static void main(String[] args) {
        Student student = new StudentDaoImpl().findStudentByLogin("123");
        System.out.println(student.toString());
    }
}
