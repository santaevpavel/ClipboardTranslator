package ru.santaev.clipboardtranslator.util;


import android.content.Context;
import android.content.SharedPreferences;

import ru.santaev.clipboardtranslator.TranslatorApp;
import ru.santaev.clipboardtranslator.model.Language;
import ru.santaev.clipboardtranslator.service.uitl.ITranslationSettingsProvider;

public class AppPreference implements ITranslationSettingsProvider{

    public static final String APP_PREF = "APP_PREF";

    public static final String KEY_ORIGIN_LANG = "KEY_ORIGIN_LANG";
    public static final String KEY_TARGET_LANG = "KEY_TARGET_LANG";

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
                .putString(KEY_ORIGIN_LANG, lang.getCode())
                .apply();
    }

    public void setTargetLang(Language lang){
        sharedPreferences.edit()
                .putString(KEY_TARGET_LANG, lang.getCode())
                .apply();
    }

    @Override
    public Language getOriginLang() {
        String code = sharedPreferences.getString(KEY_ORIGIN_LANG, Language.RUSSIAN.getCode());
        return Language.byCode(code);
    }

    @Override
    public Language getTargetLang() {
        String code = sharedPreferences.getString(KEY_TARGET_LANG, Language.ENGLISH.getCode());
        return Language.byCode(code);
    }
}
