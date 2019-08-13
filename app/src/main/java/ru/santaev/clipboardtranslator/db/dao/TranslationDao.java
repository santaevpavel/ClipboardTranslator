package ru.santaev.clipboardtranslator.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;
import ru.santaev.clipboardtranslator.db.entity.Translation;
import ru.santaev.clipboardtranslator.db.entity.TranslationContract;

@Dao
public interface TranslationDao {

    @Query("SELECT * FROM " + TranslationContract.TABLE_NAME + " WHERE id=:id")
    Translation getTranslationSync(long id);

    @Query("SELECT * FROM " + TranslationContract.TABLE_NAME + " WHERE id=:id")
    Single<Translation> getTranslation(long id);

    @Query("SELECT * FROM " + TranslationContract.TABLE_NAME + " ORDER BY id DESC")
    Flowable<List<Translation>> getTranslations();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Translation translation);

    @Delete
    void delete(Translation translation);

    @Query("DELETE from " + TranslationContract.TABLE_NAME)
    void deleteAll();
}
