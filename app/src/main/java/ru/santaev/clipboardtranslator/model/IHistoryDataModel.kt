package ru.santaev.clipboardtranslator.model

import io.reactivex.Flowable
import ru.santaev.clipboardtranslator.db.entity.Translation

interface IHistoryDataModel {

    val translationHistory: Flowable<List<Translation>>

    fun removeTranslation(translation: Translation)

    fun deleteAll()
}