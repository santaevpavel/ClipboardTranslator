package ru.santaev.clipboardtranslator.domain.repository

import com.example.santaev.domain.database.ITranslationDao
import com.example.santaev.domain.dto.LanguageDto
import com.example.santaev.domain.dto.TranslationDto
import com.example.santaev.domain.repository.TranslationRepository
import org.junit.Test
import org.mockito.Mockito
import org.mockito.internal.verification.VerificationModeFactory
import java.util.concurrent.TimeUnit

class TranslationRepositoryTest {

    @Test
    fun testInsert() {
        val sourceLanguage = LanguageDto(0, "Lang1", "l1")
        val targetLanguage = LanguageDto(1, "Lang2", "l2")

        val translationDao = Mockito.mock(ITranslationDao::class.java)

        val translationRepository = TranslationRepository(translationDao)

        val translation = TranslationDto(
                id = 0,
                sourceText = "text",
                targetText = "text2",
                sourceLangCode = sourceLanguage.code,
                targetLangCode = targetLanguage.code)

        translationRepository
                .insert(translation)
                .test()
                .await(1, TimeUnit.SECONDS)

        Mockito.verify(translationDao).insert(translation)
    }

    @Test
    fun testUpdateWithReplace() {
        val sourceLanguage = LanguageDto(0, "Lang1", "l1")
        val targetLanguage = LanguageDto(1, "Lang2", "l2")

        val translationDao = Mockito.mock(ITranslationDao::class.java)
        val translationRepository = TranslationRepository(translationDao)

        val translation = TranslationDto(
                id = 0,
                sourceText = "text",
                targetText = "text2",
                sourceLangCode = sourceLanguage.code,
                targetLangCode = targetLanguage.code)
        val translation2 = TranslationDto(
                id = 0,
                sourceText = "text text",
                targetText = "text3",
                sourceLangCode = sourceLanguage.code,
                targetLangCode = targetLanguage.code)

        Mockito.`when`(translationDao.insert(translation)).thenReturn(99)

        translationRepository
                .insert(translation)
                .test()
                .await(1, TimeUnit.SECONDS)

        Mockito.verify(translationDao).insert(translation)

        translationRepository
                .insert(translation2)
                .test()
                .await(1, TimeUnit.SECONDS)

        Mockito
                .verify(translationDao)
                .insert(translation2.copy(id = 99))
    }

    @Test
    fun testUpdateWithoutReplace() {
        val sourceLanguage = LanguageDto(0, "Lang1", "l1")
        val targetLanguage = LanguageDto(1, "Lang2", "l2")

        val translationDao = Mockito.mock(ITranslationDao::class.java)
        val translationRepository = TranslationRepository(translationDao)

        val translation = TranslationDto(
                id = 0,
                sourceText = "text",
                targetText = "text2",
                sourceLangCode = sourceLanguage.code,
                targetLangCode = targetLanguage.code)
        val translation2 = TranslationDto(
                id = 0,
                sourceText = "tex",
                targetText = "text",
                sourceLangCode = sourceLanguage.code,
                targetLangCode = targetLanguage.code)

        Mockito.`when`(translationDao.insert(translation)).thenReturn(99)

        translationRepository
                .insert(translation)
                .test()
                .await(1, TimeUnit.SECONDS)

        Mockito.verify(translationDao).insert(translation)

        translationRepository
                .insert(translation2)
                .test()
                .await(1, TimeUnit.SECONDS)

        Mockito
                .verify(translationDao)
                .insert(translation2)
        Mockito
                .verify(translationDao, VerificationModeFactory.noMoreInteractions())
                .insert(translation2.copy(id = 99))
    }

    @Test
    fun testUpdateWithoutReplace2() {
        val sourceLanguage = LanguageDto(0, "Lang1", "l1")
        val targetLanguage = LanguageDto(1, "Lang2", "l2")

        val translationDao = Mockito.mock(ITranslationDao::class.java)
        val translationRepository = TranslationRepository(translationDao)

        val translation = TranslationDto(
                id = 0,
                sourceText = "text",
                targetText = "text2",
                sourceLangCode = "l3",
                targetLangCode = targetLanguage.code)
        val translation2 = TranslationDto(
                id = 0,
                sourceText = "text",
                targetText = "text3",
                sourceLangCode = sourceLanguage.code,
                targetLangCode = targetLanguage.code)

        Mockito.`when`(translationDao.insert(translation)).thenReturn(99)

        translationRepository
                .insert(translation)
                .test()
                .await(1, TimeUnit.SECONDS)

        Mockito.verify(translationDao).insert(translation)

        translationRepository
                .insert(translation2)
                .test()
                .await(1, TimeUnit.SECONDS)

        Mockito
                .verify(translationDao)
                .insert(translation2)
        Mockito
                .verify(translationDao, VerificationModeFactory.noMoreInteractions())
                .insert(translation2.copy(id = 99))
    }
}