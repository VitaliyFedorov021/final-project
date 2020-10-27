package ua.com.alevel.dto;

public class Theme {
    private final String name;

    @Override
    public String toString() {
        return "Theme{" +
                "name='" + name + '\'' +
                '}';
    }

    public Theme(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
