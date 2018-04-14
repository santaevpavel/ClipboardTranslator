package ru.santaev.clipboardtranslator.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;
import ru.santaev.clipboardtranslator.db.entity.Language;
import ru.santaev.clipboardtranslator.db.entity.LanguageContract;

@Dao
public interface LanguageDao {

    @Query("SELECT * FROM " + LanguageContract.TABLE_NAME + " ORDER BY " + LanguageContract.NAME + " ASC")
    Flowable<List<Language>> getLanguages();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<Language> languages);

    @Query("DELETE from " + LanguageContract.TABLE_NAME)
    void deleteAll();
}
