package ua.com.alevel.services;

import ua.com.alevel.dto.Theme;
import ua.com.alevel.exceptions.IncorrectDataException;
import ua.com.alevel.exceptions.NoDataInDBException;

public interface ThemeService {
    void addTheme(Theme theme) throws IncorrectDataException;

    void deleteTheme(String themeName) throws NoDataInDBException, IncorrectDataException;
}
