package com.example.santaev.domain.repository

import com.example.santaev.domain.dto.LanguageDto
import io.reactivex.Flowable


interface ILanguageRepository {

    fun getLanguages(): Flowable<List<LanguageDto>>
}