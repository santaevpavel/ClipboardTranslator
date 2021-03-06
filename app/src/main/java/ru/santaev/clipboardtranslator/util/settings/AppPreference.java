package ru.santaev.clipboardtranslator.util.settings;


import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.Nullable;

import com.example.santaev.domain.dto.LanguageDto;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ru.santaev.clipboardtranslator.TranslatorApp;
import ru.santaev.clipboardtranslator.db.entity.Language;
import ru.santaev.clipboardtranslator.service.util.ITranslationSettingsProvider;

public class AppPreference implements ITranslationSettingsProvider, ISettings {

    private static final String APP_PREF = "APP_PREF";

    public static final String KEY_ORIGIN_LANG_NAME = "KEY_ORIGIN_LANG_NAME";
    public static final String KEY_ORIGIN_LANG_CODE = "KEY_ORIGIN_LANG_CODE";
    public static final String KEY_TARGET_LANG_NAME = "KEY_TARGET_LANG_NAME";
    public static final String KEY_TARGET_LANG_CODE = "KEY_TARGET_LANG_CODE";
    public static final String KEY_LAST_USER_LANGUAGES = "KEY_LAST_USER_LANGUAGES";
    public static final String KEY_NOTIFICATION_TYPE = "KEY_NOTIFICATION_TYPE";
    public static final String KEY_NOTIFICATION_BTN = "KEY_NOTIFICATION_BTN";
    public static final String KEY_NOTIFICATION_CLEAR_DELAY = "KEY_NOTIFICATION_CLEAR_DELAY";

    private SharedPreferences sharedPreferences;

    public static SharedPreferences getAppSharedPreference(){
        return TranslatorApp.Companion.getAppContext().getSharedPreferences(APP_PREF, Context.MODE_PRIVATE);
    }

    public static AppPreference getInstance(){
        return new AppPreference(getAppSharedPreference());
    }

    public AppPreference(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public void setOriginLang(LanguageDto lang) {
        sharedPreferences.edit()
                .putString(KEY_ORIGIN_LANG_NAME, lang.getName())
                .putString(KEY_ORIGIN_LANG_CODE, lang.getCode())
                .apply();
    }

    public void setTargetLang(LanguageDto lang) {
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


    @Nullable
    @Override
    public LanguageDto getOriginLang() {
        String code = sharedPreferences.getString(KEY_ORIGIN_LANG_CODE, null);
        String name = sharedPreferences.getString(KEY_ORIGIN_LANG_NAME, null);
        if (code != null && name != null) {
            return new LanguageDto(0, name, code);
        } else {
            return null;
        }
    }

    @Nullable
    @Override
    public LanguageDto getTargetLang() {
        String code = sharedPreferences.getString(KEY_TARGET_LANG_CODE, null);
        String name = sharedPreferences.getString(KEY_TARGET_LANG_NAME, null);
        if (code != null && name != null) {
            return new LanguageDto(0, name, code);
        } else {
            return null;
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

    @Override
    public boolean isNotificationButtonEnabled() {
        return sharedPreferences.getBoolean(KEY_NOTIFICATION_BTN, true);
    }

    @Override
    public void setNotificationButtonEnabled(boolean enabled) {
        sharedPreferences.edit()
                .putBoolean(KEY_NOTIFICATION_BTN, enabled)
                .apply();
    }

    @Override
    public int getNotificationClearDelay() {
        return sharedPreferences.getInt(KEY_NOTIFICATION_CLEAR_DELAY, ISettings.NOTIFICATION_DELAY_NONE);
    }

    @Override
    public void setNotificationClearDelay(int delay) {
        sharedPreferences.edit()
                .putInt(KEY_NOTIFICATION_CLEAR_DELAY, delay)
                .apply();
    }
}
