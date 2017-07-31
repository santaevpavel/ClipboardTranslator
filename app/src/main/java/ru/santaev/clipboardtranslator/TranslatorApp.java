package ru.santaev.clipboardtranslator;


import android.app.Application;
import android.content.Context;

import ru.santaev.clipboardtranslator.db.AppDatabase;

public class TranslatorApp extends Application{

    private static TranslatorApp instance;

    public static TranslatorApp getInstance(){
        return instance;
    }

    public static Context getAppContext(){
        return getInstance().getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        AppDatabase.init(getAppContext());
    }

}
