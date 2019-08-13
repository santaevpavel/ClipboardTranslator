package ru.santaev.clipboardtranslator.db.entity


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

import ru.santaev.clipboardtranslator.db.entity.TranslationContract.LANG_SOURCE
import ru.santaev.clipboardtranslator.db.entity.TranslationContract.LANG_TARGET
import ru.santaev.clipboardtranslator.db.entity.TranslationContract.TEXT_SOURCE
import ru.santaev.clipboardtranslator.db.entity.TranslationContract.TEXT_TARGET

@Entity(tableName = TranslationContract.TABLE_NAME)
class Translation(
        @PrimaryKey(autoGenerate = true) var id: Long = 0,
        @ColumnInfo(name = LANG_SOURCE) var langSource: String?,
        @ColumnInfo(name = LANG_TARGET) var langTarget: String?,
        @ColumnInfo(name = TEXT_SOURCE) var textSource: String?,
        @ColumnInfo(name = TEXT_TARGET) var textTarget: String?
) {

    @Ignore
    constructor(
            langSource: String,
            langTarget: String,
            textSource: String,
            textTarget: String
    ) : this(0, langSource, langTarget, textSource, textTarget)
}

