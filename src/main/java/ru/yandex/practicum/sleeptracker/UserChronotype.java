package ru.yandex.practicum.sleeptracker;

public enum UserChronotype {
    OWL("сова"),
    LARK("жаворонок"),
    PIGEON("голубь");

    private final String displayName;

    UserChronotype(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}