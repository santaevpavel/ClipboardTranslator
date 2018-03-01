package ru.santaev.clipboardtranslator.model.repository.database

import ru.santaev.clipboardtranslator.db.dao.ILanguageDao
import ru.santaev.clipboardtranslator.db.dao.ITranslationDao

interface IAppDatabase {

    fun getLanguageDao(): ILanguageDao

    fun getTranslationDao(): ITranslationDao
}