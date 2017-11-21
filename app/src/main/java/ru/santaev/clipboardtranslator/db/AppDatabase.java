package ru.santaev.clipboardtranslator.db;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import ru.santaev.clipboardtranslator.db.dao.LanguageDao;
import ru.santaev.clipboardtranslator.db.dao.TranslationDao;
import ru.santaev.clipboardtranslator.db.entity.Language;
import ru.santaev.clipboardtranslator.db.entity.Translation;

@Database(entities = {Translation.class, Language.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase{

    public static final String DB_NAME = "TranslationDB";

    public abstract TranslationDao getTranslationDao();

    public abstract LanguageDao getLanguageDao();
}
