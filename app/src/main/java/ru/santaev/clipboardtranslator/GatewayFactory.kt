package ru.santaev.clipboardtranslator

import com.example.santaev.domain.api.IApiService
import com.example.santaev.domain.database.ILanguageDao
import com.example.santaev.domain.database.ITranslationDao
import com.example.santaev.domain.dto.LanguageDto
import com.example.santaev.domain.dto.TranslationDto
import com.example.santaev.domain.factory.IGatewayFactory
import io.reactivex.Flowable
import io.reactivex.Single
import ru.santaev.clipboardtranslator.db.dao.LanguageDao
import ru.santaev.clipboardtranslator.db.dao.TranslationDao
import ru.santaev.clipboardtranslator.db.entity.Language
import ru.santaev.clipboardtranslator.db.entity.Translation

class GatewayFactory(
        private val languageDao: LanguageDao,
        private val translationDao: TranslationDao,
        private val apiService: IApiService
) : IGatewayFactory {

    override fun getLanguageDao(): ILanguageDao = object : ILanguageDao {

        override fun getLanguages(): Flowable<List<LanguageDto>> {
            return languageDao
                    .languages
                    .map { list ->
                        list.map { LanguageDto(it.id, it.name!!, it.code!!) }
                    }
        }

        override fun insertAll(languages: List<LanguageDto>): LongArray {
            return languageDao
                    .insertAll(languages.map { Language(it.id, it.name, it.code) })
        }

    }

    override fun getTranslationDao(): ITranslationDao = object : ITranslationDao {

        override fun getTranslations(): Flowable<List<TranslationDto>> = translationDao
                .translations
                .map { entities -> entities.map { it.toDto() } }

        override fun getTranslation(id: Long): Single<TranslationDto> = translationDao
                .getTranslation(id)
                .map { it.toDto() }

        override fun insert(translation: TranslationDto): Long = translationDao
                .insert(translation.toDbEntity())

        override fun delete(translation: TranslationDto) = translationDao
                .delete(translation.toDbEntity())

        override fun deleteAll() = translationDao.deleteAll()

    }

    override fun getApiService(): IApiService = apiService
}

private fun Translation.toDto(): TranslationDto {
    return TranslationDto(
            id,
            emptyIfNull(langSource),
            emptyIfNull(langTarget),
            emptyIfNull(textSource),
            emptyIfNull(textTarget)
    )
}

private fun TranslationDto.toDbEntity(): Translation {
    val translation = Translation(
            sourceLangCode,
            targetLangCode,
            sourceText,
            targetText
    )
    translation.id = id
    return translation
}

private fun emptyIfNull(string: String?): String = string ?: ""
