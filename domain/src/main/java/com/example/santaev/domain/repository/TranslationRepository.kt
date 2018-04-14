package com.example.santaev.domain.repository

import com.example.santaev.domain.database.ITranslationDao
import com.example.santaev.domain.dto.TranslationDto
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single


internal class TranslationRepository(
        private val translationDao: ITranslationDao
) : ITranslationRepository {

    private var lastTranslation: TranslationDto? = null

    override fun getTranslation(id: Long): Single<TranslationDto> = translationDao.getTranslation(id)

    override fun getTranslations(): Flowable<List<TranslationDto>> = translationDao.getTranslations()

    /**
     * If last translation in DB and `translation` languages are same and last
     * translations text is part of `translation` text then new row will not be added
     */
    override fun insert(translation: TranslationDto): Single<Long> {
        return Single.fromCallable {
            synchronized(this, {
                val lastTranslation = lastTranslation
                val toUpdate = if (lastTranslation != null && isNeedToUpdate(translation)) {
                    translation.copy(id = lastTranslation.id)
                } else {
                    translation
                }
                translationDao.insert(toUpdate).also { id ->
                    this.lastTranslation = toUpdate.copy(id = id)
                }
            })
        }
    }

    override fun delete(translation: TranslationDto): Completable {
        return Completable.fromAction {
            synchronized(this) {
                lastTranslation = null
                translationDao.delete(translation)
            }
        }
    }

    override fun deleteAll(): Completable {
        return Completable.fromAction {
            lastTranslation = null
            translationDao.deleteAll()
        }
    }

    private fun isNeedToUpdate(translation: TranslationDto): Boolean {
        return lastTranslation?.let { last ->
            last.sourceLangCode == translation.sourceLangCode
                    && last.targetLangCode == translation.targetLangCode
                    && translation.sourceText.contains(last.sourceText)
        } ?: false
    }
}