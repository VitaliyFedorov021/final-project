package ua.com.alevel.services.impl;

import ua.com.alevel.util.Checker;
import ua.com.alevel.dao.ThemeDao;
import ua.com.alevel.dao.impl.ThemeDaoImpl;
import ua.com.alevel.dto.Theme;
import ua.com.alevel.exceptions.IncorrectDataException;
import ua.com.alevel.exceptions.NoDataInDBException;
import ua.com.alevel.services.ThemeService;

public class ThemeServiceImpl implements ThemeService {
    ThemeDao themeDao = ThemeDaoImpl.getInstance();
    @Override
    public void addTheme(Theme theme) throws IncorrectDataException {
        if (Checker.isCorrectName(theme.getName())) {
            themeDao.addTheme(theme);
            return;
        }
        throw new IncorrectDataException("Incorrect theme name");

    }

    @Override
    public void deleteTheme(String themeName) throws NoDataInDBException, IncorrectDataException {
        if (Checker.isCorrectName(themeName)) {
            themeDao.deleteTheme(themeName);
            return;
        }
        throw new IncorrectDataException("Incorrect theme name");
    }
}
