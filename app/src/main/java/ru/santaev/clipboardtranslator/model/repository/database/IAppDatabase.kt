package ru.santaev.clipboardtranslator.model.repository.database

import ru.santaev.clipboardtranslator.db.dao.LanguageDao
import ru.santaev.clipboardtranslator.db.dao.TranslationDao

interface IAppDatabase {

    fun getLanguageDao(): LanguageDao

    fun getTranslationDao(): TranslationDao
}