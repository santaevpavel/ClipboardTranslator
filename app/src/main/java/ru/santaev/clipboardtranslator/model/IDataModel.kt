package ru.santaev.clipboardtranslator.model


import io.reactivex.Flowable
import io.reactivex.Single
import ru.santaev.clipboardtranslator.db.entity.Language

interface IDataModel {

    class TranslateResponse(var targetText: String)

    val languages: Flowable<List<Language>>

    fun translate(originLang: Language, targetLang: Language, originText: String): Single<TranslateResponse>

}
