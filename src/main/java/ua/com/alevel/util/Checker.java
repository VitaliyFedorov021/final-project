package ua.com.alevel.util;

import java.util.Date;

public class Checker {
    public static boolean isCorrectName(String name) {
        if (name.isBlank()) {
            return false;
        }
        return true;

    }

    public static boolean isCorrectNumber(int number) {
        if (number < 0) {
            return false;
        }
        return true;
    }

    public static boolean isCorrectDate(Date startDate, Date endDate) {
        if (startDate.compareTo(endDate) == 1 ) {
            return false;
        }
        return true;
    }
}
