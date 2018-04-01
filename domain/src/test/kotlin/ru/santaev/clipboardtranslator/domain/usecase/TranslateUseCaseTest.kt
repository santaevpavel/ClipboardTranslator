package ru.santaev.clipboardtranslator.domain.usecase

import com.example.santaev.domain.dto.LanguageDto
import com.example.santaev.domain.dto.TranslationDto
import com.example.santaev.domain.repository.ITranslationRepository
import com.example.santaev.domain.usecase.TranslateUseCase
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito
import ru.santaev.clipboardtranslator.domain.MockApiService

class TranslateUseCaseTest {

    @Test
    fun execute() {
        val sourceLanguage = LanguageDto(0, "Lang1", "l1")
        val targetLanguage = LanguageDto(2, "Lang2", "l2")

        val translationRepository = Mockito.mock(ITranslationRepository::class.java)
        val translation = TranslationDto(
                id = 0,
                sourceText = "text",
                targetText = "translated text from l1 to l2",
                sourceLangCode = sourceLanguage.code,
                targetLangCode = targetLanguage.code)
        Mockito
                .`when`(translationRepository.insert(translation))
                .thenReturn(Single.just(1))

        val useCase = TranslateUseCase(
                sourceLanguage,
                targetLanguage,
                "text",
                MockApiService(),
                translationRepository
        )

        val expected = TranslateUseCase.Response.Success(
                "text",
                "translated text from l1 to l2"
        )
        val testObserver = useCase
                .execute()
                .test()
        testObserver.assertComplete()
        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)

        val response = testObserver.values().first()
        assertEquals(expected, response)

        Mockito
                .verify(translationRepository)
                .insert(translation)
    }

}