package ua.com.alevel.util;

import java.util.Scanner;

public class Reader {
    private static final String COLOR = (char) 27 + "[34m";
    public static String input(String message) {
        Scanner sc = new Scanner(System.in);
        if (message == null) {
            System.out.print("");
        } else {
            System.out.println(COLOR + message);
        }
        String str = sc.nextLine();
        return str;
    }
}
