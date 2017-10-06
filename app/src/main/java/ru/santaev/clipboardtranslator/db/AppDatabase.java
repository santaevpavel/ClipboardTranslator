package ru.santaev.clipboardtranslator.db;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import ru.santaev.clipboardtranslator.TranslatorApp;
import ru.santaev.clipboardtranslator.db.dao.LanguageDao;
import ru.santaev.clipboardtranslator.db.dao.TranslationDao;
import ru.santaev.clipboardtranslator.db.entity.Language;
import ru.santaev.clipboardtranslator.db.entity.Translation;

@Database(entities = {Translation.class, Language.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase{

    private static final String DB_NAME = "TranslationDB";
    private static AppDatabase instance;

    public static AppDatabase getInstance(){
        return instance;
    }

    public static void init(Context context){
        if (instance != null){
            throw new RuntimeException("You should call init() only once");
        }
        instance = Room.databaseBuilder(TranslatorApp.getAppContext(),
                AppDatabase.class, DB_NAME).build();
    }

    public abstract TranslationDao getTranslationDao();

    public abstract LanguageDao getLanguageDao();
}
