package ru.santaev.clipboardtranslator.util;


import android.content.Context;
import android.content.SharedPreferences;

import ru.santaev.clipboardtranslator.TranslatorApp;
import ru.santaev.clipboardtranslator.db.entity.Language;
import ru.santaev.clipboardtranslator.service.uitl.ITranslationSettingsProvider;

public class AppPreference implements ITranslationSettingsProvider{

    public static final String APP_PREF = "APP_PREF";

    public static final String KEY_ORIGIN_LANG_NAME = "KEY_ORIGIN_LANG_NAME";
    public static final String KEY_ORIGIN_LANG_CODE = "KEY_ORIGIN_LANG_CODE";
    public static final String KEY_TARGET_LANG_NAME = "KEY_TARGET_LANG_NAME";
    public static final String KEY_TARGET_LANG_CODE = "KEY_TARGET_LANG_CODE";

    private SharedPreferences sharedPreferences;

    public static SharedPreferences getAppSharedPreference(){
        return TranslatorApp.getAppContext().getSharedPreferences(APP_PREF, Context.MODE_PRIVATE);
    }

    public static AppPreference getInstance(){
        return new AppPreference(getAppSharedPreference());
    }

    public AppPreference(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public void setOriginLang(Language lang){
        sharedPreferences.edit()
                .putString(KEY_ORIGIN_LANG_NAME, lang.getName())
                .putString(KEY_ORIGIN_LANG_CODE, lang.getCode())
                .apply();
    }

    public void setTargetLang(Language lang){
        sharedPreferences.edit()
                .putString(KEY_TARGET_LANG_NAME, lang.getName())
                .putString(KEY_TARGET_LANG_CODE, lang.getCode())
                .apply();
    }

    @Override
    public Language getOriginLang() {
        String code = sharedPreferences.getString(KEY_ORIGIN_LANG_CODE, null);
        String name = sharedPreferences.getString(KEY_ORIGIN_LANG_NAME, null);
        if (code != null && name != null) {
            return new Language(code, name);
        } else {
            return new Language("ru", "Русский");
        }
    }

    @Override
    public Language getTargetLang() {
        String code = sharedPreferences.getString(KEY_TARGET_LANG_CODE, null);
        String name = sharedPreferences.getString(KEY_TARGET_LANG_NAME, null);
        if (code != null && name != null) {
            return new Language(code, name);
        } else {
            return new Language("en", "Английский");
        }
    }
}
