package ua.com.alevel.dto;

import java.util.Date;

public class Course {
    private final int id;
    private final String name, theme;
    private final Date startDate, endDate;

    public Course(int id, String name, String theme, Date startDate, Date endDate) {
        this.id = id;
        this.name = name;
        this.theme = theme;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", theme='" + theme + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTheme() {
        return theme;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }
}
