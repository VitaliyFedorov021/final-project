package ua.com.alevel.run;

import org.apache.log4j.Logger;
import ua.com.alevel.commands.Command;
import ua.com.alevel.commands.impl.AdminCommand;
import ua.com.alevel.commands.impl.StudentCommand;
import ua.com.alevel.commands.impl.TeacherCommand;

import java.util.Scanner;


public class Runner {
    private static final String COLOR = (char) 27 + "[34m";

    private Logger log = Logger.getLogger(Runner.class);
    Command studentCommand = new StudentCommand();
    Command adminCommand = new AdminCommand();
    Command teacherCommand = new TeacherCommand();
    private String input(String message) {
        Scanner sc = new Scanner(System.in);
        if (message == null) {
            System.out.print("");
        } else {
            System.out.println(message);
        }
        String str = sc.nextLine();
        return str;
    }

    public void action() {
        int a = 0;
        try {
            a = Integer.parseInt(input(COLOR + "Choose a mode (1 - Student, 2 - Teacher, 3 - Admin) 0 - End program"));
        } catch (NumberFormatException e) {
            System.err.println("Incorrect action, try again");
            action();
        }
        while (true) {
            switch (a) {
                case 1: {
                    studentCommand.execute();
                }
                case 2: {
                    teacherCommand.execute();
                }
                case 3: {
                    adminCommand.execute();
                }
                case 0: {
                    System.exit(1);
                }
                default: {
                    System.err.println("Incorrect input");
                }
            }
        }
    }

}
