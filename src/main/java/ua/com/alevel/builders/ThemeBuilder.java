package ua.com.alevel.builders;

import ua.com.alevel.dto.Theme;

public interface ThemeBuilder {
    ThemeBuilder setName(String name);

    Theme build();
}
