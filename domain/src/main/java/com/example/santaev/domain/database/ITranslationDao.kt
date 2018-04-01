package com.example.santaev.domain.database

import com.example.santaev.domain.dto.TranslationDto

import io.reactivex.Flowable
import io.reactivex.Single

interface ITranslationDao {

    fun getTranslations(): Flowable<List<TranslationDto>>

    fun getTranslation(id: Long): Single<TranslationDto>

    fun insert(translation: TranslationDto): Long

    fun delete(translation: TranslationDto)

    fun deleteAll()
}
