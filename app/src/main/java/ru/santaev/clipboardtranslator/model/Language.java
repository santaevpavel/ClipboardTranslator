package ru.santaev.clipboardtranslator.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Language {

    RUSSIAN("ru", 0),
    ENGLISH("en", 0),
    UKRAINIAN("uk", 0),
    GERMAN("ge", 0),
    BELORUSSIAN("be", 0),
    ITALIAN("it", 0),
    SPAIN("es", 0),
    FRENCH("fr", 0),
    HOLLAND("nl", 0),
    PORTUGAL("pt", 0);

    private static String[] allowedTranslates = { "az-ru", "be-bg", "be-cs",
            "be-de", "be-en", "be-es", "be-fr", "be-it", "be-pl", "be-ro",
            "be-ru", "be-sr", "be-tr", "bg-be", "bg-ru", "bg-uk", "ca-en",
            "ca-ru", "cs-be", "cs-en", "cs-ru", "cs-uk", "da-en", "da-ru",
            "de-be", "de-en", "de-es", "de-fr", "de-it", "de-ru", "de-tr",
            "de-uk", "el-en", "el-ru", "en-be", "en-ca", "en-cs", "en-da",
            "en-de", "en-el", "en-es", "en-et", "en-fi", "en-fr", "en-hu",
            "en-it", "en-lt", "en-lv", "en-mk", "en-nl", "en-no", "en-pt",
            "en-ru", "en-sk", "en-sl", "en-sq", "en-sv", "en-tr", "en-uk",
            "es-be", "es-de", "es-en", "es-ru", "es-uk", "et-en", "et-ru",
            "fi-en", "fi-ru", "fr-be", "fr-de", "fr-en", "fr-ru", "fr-uk",
            "hr-ru", "hu-en", "hu-ru", "hy-ru", "it-be", "it-de", "it-en",
            "it-ru", "it-uk", "lt-en", "lt-ru", "lv-en", "lv-ru", "mk-en",
            "mk-ru", "nl-en", "nl-ru", "no-en", "no-ru", "pl-be", "pl-ru",
            "pl-uk", "pt-en", "pt-ru", "ro-be", "ro-ru", "ro-uk", "ru-az",
            "ru-be", "ru-bg", "ru-ca", "ru-cs", "ru-da", "ru-de", "ru-el",
            "ru-en", "ru-es", "ru-et", "ru-fi", "ru-fr", "ru-hr", "ru-hu",
            "ru-hy", "ru-it", "ru-lt", "ru-lv", "ru-mk", "ru-nl", "ru-no",
            "ru-pl", "ru-pt", "ru-ro", "ru-sk", "ru-sl", "ru-sq", "ru-sr",
            "ru-sv", "ru-tr", "ru-uk", "sk-en", "sk-ru", "sl-en", "sl-ru",
            "sq-en", "sq-ru", "sr-be", "sr-ru", "sr-uk", "sv-en", "sv-ru",
            "tr-be", "tr-de", "tr-en", "tr-ru", "tr-uk", "uk-bg", "uk-cs",
            "uk-de", "uk-en", "uk-es", "uk-fr", "uk-it", "uk-pl", "uk-ro",
            "uk-ru", "uk-sr", "uk-tr" };

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

    public List<Language> getSupportedTargetLangs(){
        ArrayList<Language> res = new ArrayList<>();
        for (Language language : values()) {
            if (language != this && isTranslateFromTo(this, language)){
                res.add(language);
            }
        }
        return res;
    }

    public static Language byCode(String code){
        for (Language language : values()) {
            if (language.getCode().equals(code)){
                return language;
            }
        }
        return null;
    }

    public static boolean isTranslateFromTo(Language langFrom, Language langTo) {
        return 0 <= Arrays.binarySearch(allowedTranslates, langFrom.getCode() + "-" + langTo.getCode());
    }

    public static ArrayList<Language> getAllLangs() {
        ArrayList<Language> a = new ArrayList<Language>();
        a.add(Language.RUSSIAN);
        a.add(Language.UKRAINIAN);
        a.add(Language.BELORUSSIAN);
        a.add(Language.ITALIAN);
        a.add(Language.SPAIN);
        a.add(Language.ENGLISH);
        a.add(Language.FRENCH);
        a.add(Language.GERMAN);
        a.add(Language.HOLLAND);
        a.add(Language.PORTUGAL);

        return a;
    }

}
