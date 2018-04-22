package com.example.santaev.domain.repository

import com.example.santaev.domain.api.IApiService
import com.example.santaev.domain.api.LanguagesResponseDto
import com.example.santaev.domain.database.ILanguageDao
import com.example.santaev.domain.dto.LanguageDto
import com.example.santaev.domain.repository.ILanguageRepository.LanguagesState
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

internal class LanguageRepository(
        private val languageDao: ILanguageDao,
        private val apiService: IApiService
) : ILanguageRepository {

    private var languagesLoadingStatus: LanguagesState? = null
        set(value) {
            field = value
            if (value != null) languagesStatusSubject.onNext(value)
        }
    private val languagesStatusSubject = PublishSubject.create<LanguagesState>()

    override fun requestLanguages(): Observable<LanguagesState> {
        loadLanguages()
        return languagesStatusSubject
    }

    override fun getLanguages(): Flowable<List<LanguageDto>> {
        loadLanguages()
        return languageDao.getLanguages()
    }

    override fun deleteAll(): Completable = Completable.fromAction { languageDao.getLanguages() }

    private fun loadLanguages() {
        if (languagesLoadingStatus == LanguagesState.LOADING) return
        languagesLoadingStatus = LanguagesState.LOADING

        apiService.getLanguages()
                .subscribeOn(Schedulers.io())
                .subscribe(
                        this::onLoadLanguages,
                        { error ->
                            error.printStackTrace()
                            languagesLoadingStatus = LanguagesState.ERROR
                        }
                )
    }

    private fun onLoadLanguages(languagesResponse: LanguagesResponseDto) {
        languagesLoadingStatus = LanguagesState.SUCCESS
        languageDao.insertAll(languagesResponse.languages)
    }

}
