package ru.santaev.clipboardtranslator.db.entity;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import static ru.santaev.clipboardtranslator.db.entity.TranslationContract.*;

@Entity(tableName = TranslationContract.TABLE_NAME)
public class Translation {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = LANG_SOURCE)
    private String langSource;

    @ColumnInfo(name = LANG_TARGET)
    private String langTarget;

    @ColumnInfo(name = TEXT_SOURCE)
    private String textSource;

    @ColumnInfo(name = TEXT_TARGET)
    private String textTarget;

    public Translation(String langSource, String langTarget, String textSource, String textTarget) {
        this.langSource = langSource;
        this.langTarget = langTarget;
        this.textSource = textSource;
        this.textTarget = textTarget;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setLangSource(String langSource) {
        this.langSource = langSource;
    }

    public void setLangTarget(String langTarget) {
        this.langTarget = langTarget;
    }

    public void setTextSource(String textSource) {
        this.textSource = textSource;
    }

    public void setTextTarget(String textTarget) {
        this.textTarget = textTarget;
    }

    public long getId() {
        return id;
    }

    public String getLangSource() {
        return langSource;
    }

    public String getLangTarget() {
        return langTarget;
    }

    public String getTextSource() {
        return textSource;
    }

    public String getTextTarget() {
        return textTarget;
    }
}

