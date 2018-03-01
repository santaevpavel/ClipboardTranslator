package ru.santaev.clipboardtranslator.model


import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import ru.santaev.clipboardtranslator.api.IApiService
import ru.santaev.clipboardtranslator.api.TranslateRequest
import ru.santaev.clipboardtranslator.db.entity.Language
import ru.santaev.clipboardtranslator.db.entity.Translation
import ru.santaev.clipboardtranslator.model.repository.ILanguageRepository
import ru.santaev.clipboardtranslator.model.repository.database.IAppDatabase

class DataModel(
        private val apiService: IApiService,
        private val appDatabase: IAppDatabase,
        private val languageDao: ILanguageRepository
) : IDataModel {

    private var lastTranslation: Translation? = null

    override val languages: Flowable<List<Language>>
        get() = languageDao.getLanguages()

    override fun translate(originLang: Language, targetLang: Language,
                           originText: String): Single<IDataModel.TranslateResponse> {

        val request = TranslateRequest(originText, originLang.code,
                targetLang.code)

        return apiService.translate(request)
                .map { response ->
                    val targetText = response.text?.get(0) ?: ""
                    saveTransition(originText, targetText, originLang, targetLang)
                    IDataModel.TranslateResponse(targetText)
                }.subscribeOn(Schedulers.io())
    }

    private fun saveTransition(finalOriginText: String, targetText: String,
                               finalOriginLang: Language, finalTargetLang: Language) {

        val newTranslation = Translation(finalOriginLang.code,
                finalTargetLang.code, finalOriginText, targetText)

        lastTranslation?.let {
            if (finalOriginText.contains(it.textSource)) {
                val savedTranslation = appDatabase
                        .getTranslationDao().getTranslationSync(it.id)
                if (savedTranslation != null) {
                    newTranslation.id = it.id
                }
            }
        }
        lastTranslation = newTranslation

        val id = appDatabase.getTranslationDao().insert(lastTranslation)
        id.let { lastTranslation?.id = it }
    }
}
