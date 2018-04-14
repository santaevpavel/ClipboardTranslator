package com.example.santaev.domain.repository

import com.example.santaev.domain.dto.LanguageDto
import io.reactivex.Flowable
import io.reactivex.Observable


interface ILanguageRepository {

    fun getLanguages(): Flowable<List<LanguageDto>>

    fun requestLanguages(): Observable<LanguagesState>

    enum class LanguagesState {
        LOADING,
        SUCCESS,
        ERROR
    }
}