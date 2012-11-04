package application.core;

import java.util.Locale;

public enum Language {
    DEUTSCH(Locale.GERMAN, "Deutsch"), ENGLISH(Locale.ENGLISH, "English");

    private Locale locale;
    private String label;

    Language(Locale locale, String label) {
        this.locale = locale;
        this.label = label;
    }

    public Locale getLocale() {
        return locale;
    }

    @Override
    public String toString() {
        return label;
    }
}
