package ru.santaev.clipboardtranslator.db.entity;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

import static ru.santaev.clipboardtranslator.db.entity.LanguageContract.CODE;
import static ru.santaev.clipboardtranslator.db.entity.LanguageContract.NAME;

@Entity(tableName = LanguageContract.TABLE_NAME,
        indices = {@Index(value = {NAME}, unique = true)})
public class Language implements Serializable{

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = NAME)
    private String name;

    @ColumnInfo(name = CODE)
    private String code;

    public Language(long id, String code, String name) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    @Ignore
    public Language(String code, String name) {
        this.name = name;
        this.code = code;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }
}

