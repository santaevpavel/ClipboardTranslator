package com.example.santaev.domain.repository

import com.example.santaev.domain.api.IApiService
import com.example.santaev.domain.api.LanguagesResponseDto
import com.example.santaev.domain.database.ILanguageDao
import com.example.santaev.domain.dto.LanguageDto
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers

internal class LanguageRepository(
        private val languageDao: ILanguageDao,
        private val apiService: IApiService
) : ILanguageRepository {

    override fun getLanguages(): Flowable<List<LanguageDto>> {
        loadLanguages()

        return languageDao.getLanguages()
    }

    private fun loadLanguages() {
        apiService.getLanguages()
                .subscribeOn(Schedulers.io())
                .subscribe(this::onLoadLanguages, { it.printStackTrace() })
    }

    private fun onLoadLanguages(languagesResponse: LanguagesResponseDto) {
        languageDao.insertAll(languagesResponse.languages)
    }

}
