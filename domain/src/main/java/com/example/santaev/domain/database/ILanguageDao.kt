package com.example.santaev.domain.database

import com.example.santaev.domain.dto.LanguageDto
import io.reactivex.Flowable

interface ILanguageDao {

    fun getLanguages(): Flowable<List<LanguageDto>>

    fun insertAll(languages: List<LanguageDto>): LongArray

}
