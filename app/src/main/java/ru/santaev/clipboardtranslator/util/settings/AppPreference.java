package ru.santaev.clipboardtranslator.util.settings;


import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ru.santaev.clipboardtranslator.TranslatorApp;
import ru.santaev.clipboardtranslator.db.entity.Language;
import ru.santaev.clipboardtranslator.service.uitl.ITranslationSettingsProvider;

public class AppPreference implements ITranslationSettingsProvider, ISettings {

    public static final String APP_PREF = "APP_PREF";

    public static final String KEY_ORIGIN_LANG_NAME = "KEY_ORIGIN_LANG_NAME";
    public static final String KEY_ORIGIN_LANG_CODE = "KEY_ORIGIN_LANG_CODE";
    public static final String KEY_TARGET_LANG_NAME = "KEY_TARGET_LANG_NAME";
    public static final String KEY_TARGET_LANG_CODE = "KEY_TARGET_LANG_CODE";
    public static final String KEY_LAST_USER_LANGUAGES = "KEY_LAST_USER_LANGUAGES";
    public static final String KEY_NOTIFICATION_TYPE = "KEY_NOTIFICATION_TYPE";

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

    public void setLastUsedLanguages(List<Language> codes) {
        sharedPreferences.edit()
                .putString(KEY_LAST_USER_LANGUAGES, new Gson().toJson(codes))
                .apply();
    }

    public void setNotifcation(List<Language> codes) {
        sharedPreferences.edit()
                .putString(KEY_LAST_USER_LANGUAGES, new Gson().toJson(codes))
                .apply();
    }


    public List<Language> getLastUsedLanguages() {
        String languages = sharedPreferences.getString(KEY_LAST_USER_LANGUAGES, "");

        Type listType = new TypeToken<ArrayList<Language>>() {
        }.getType();
        List<Language> recent = new Gson().fromJson(languages, listType);
        return recent != null ? recent : new ArrayList<>();
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

    @Override
    public int getNotificationType() {
        return sharedPreferences.getInt(KEY_NOTIFICATION_TYPE, ISettings.NOTIFICATION_TYPE_PUSH);
    }

    @Override
    public void setNotificationType(int type) {
        sharedPreferences
                .edit()
                .putInt(KEY_NOTIFICATION_TYPE, type)
                .apply();
    }
}
