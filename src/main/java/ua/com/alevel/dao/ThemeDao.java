package ua.com.alevel.dao;

import ua.com.alevel.dto.Theme;

public interface ThemeDao {
    void addTheme(Theme theme);

    void deleteTheme(String themeName);


}
