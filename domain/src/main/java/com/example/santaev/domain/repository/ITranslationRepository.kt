package com.example.santaev.domain.repository

import com.example.santaev.domain.dto.TranslationDto
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single


interface ITranslationRepository {

    fun getTranslation(id: Long): Single<TranslationDto>

    fun getTranslations(): Flowable<List<TranslationDto>>

    fun insert(translation: TranslationDto): Single<Long>

    fun delete(translation: TranslationDto): Completable

    fun deleteAll(): Completable
}