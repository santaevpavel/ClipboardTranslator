package ru.santaev.clipboardtranslator.model.repository

import io.reactivex.Flowable
import ru.santaev.clipboardtranslator.db.entity.Language


interface ILanguageRepository {

    fun getLanguages(): Flowable<List<Language>>
}