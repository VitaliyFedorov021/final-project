package ua.com.alevel.builders.impl;

import ua.com.alevel.builders.ThemeBuilder;
import ua.com.alevel.dto.Theme;

public class ThemeBuilderImpl implements ThemeBuilder {
    private String name;

    @Override
    public ThemeBuilder setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public Theme build() {
        return new Theme(name);
    }
}
