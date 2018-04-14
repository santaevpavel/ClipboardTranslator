package ru.santaev.clipboardtranslator.db;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import ru.santaev.clipboardtranslator.db.entity.Language;
import ru.santaev.clipboardtranslator.db.entity.Translation;
import ru.santaev.clipboardtranslator.model.repository.database.IAppDatabase;

@Database(entities = {Translation.class, Language.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase implements IAppDatabase {

    public static final String DB_NAME = "TranslationDB";
}
