package ru.santaev.clipboardtranslator.model;

import ru.santaev.clipboardtranslator.R;

public enum Language {

    RUSSIAN("ru", R.string.lang_russian),
    ENGLISH("en", R.string.lang_english),
    UKRAINIAN("uk", R.string.lang_ukrainian),
    GERMAN("ge", R.string.lang_german),
    BELORUSSIAN("be", R.string.lang_belorussian),
    ITALIAN("it", R.string.lang_italian),
    SPAIN("es", R.string.lang_spain),
    FRENCH("fr", R.string.lang_french),
    HOLLAND("nl", R.string.lang_holland),
    PORTUGAL("pt", R.string.lang_portugal);

    private String code;
    private int textRes;

    Language(String code, int textRes) {
        this.code = code;
        this.textRes = textRes;
    }

    public String getCode() {
        return code;
    }

    public int getTextRes() {
        return textRes;
    }

    public static Language byCode(String code){
        for (Language language : values()) {
            if (language.getCode().equals(code)){
                return language;
            }
        }
        return null;
    }

}
