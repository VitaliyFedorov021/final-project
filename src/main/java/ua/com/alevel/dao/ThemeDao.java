package ua.com.alevel.dao;

import ua.com.alevel.dto.Theme;
import ua.com.alevel.exceptions.NoDataInDBException;

public interface ThemeDao {
    void addTheme(Theme theme);

    void deleteTheme(String themeName) throws NoDataInDBException;


}
