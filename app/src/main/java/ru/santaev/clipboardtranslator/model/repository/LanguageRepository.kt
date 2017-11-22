package ru.santaev.clipboardtranslator.model.repository

import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import ru.santaev.clipboardtranslator.TranslatorApp
import ru.santaev.clipboardtranslator.api.IApiService
import ru.santaev.clipboardtranslator.api.LanguagesResponse
import ru.santaev.clipboardtranslator.db.dao.LanguageDao
import ru.santaev.clipboardtranslator.db.entity.Language

class LanguageRepository(private val apiService: IApiService) {

    private var languages: Flowable<List<Language>>? = null
    private val languageDao: LanguageDao = TranslatorApp.instance.database.languageDao

    fun getLanguages(): Flowable<List<Language>> {
        loadLanguages()

        languages.let {
            return if (it == null) {
                val newLanguages: Flowable<List<Language>> = languageDao.languages
                languages = newLanguages
                return newLanguages
            } else {
                it
            }
        }
    }

    private fun loadLanguages() {
        Single.fromCallable { apiService.languages }
                .subscribeOn(Schedulers.io())
                .subscribe(this::onLoadLanguages, { it.printStackTrace() })
    }

    private fun onLoadLanguages(languagesResponse: LanguagesResponse) {
        val languagesRaw = languagesResponse.languages
        val languages = languagesRaw?.keys?.map { Language(0, it, languagesRaw[it]) }
        languageDao.insertAll(languages)
    }

}
