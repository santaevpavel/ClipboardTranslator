package ru.santaev.clipboardtranslator.domain.usecase

import com.example.santaev.domain.dto.LanguageDto
import com.example.santaev.domain.usecase.TranslateUseCase
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import ru.santaev.clipboardtranslator.domain.MockApiService

class TranslateUseCaseTest {

    @Before
    fun setUp() {
    }

    @Test
    fun execute() {
        val sourceLanguage = LanguageDto(0, "Lang1", "l1")
        val targetLanguage = LanguageDto(2, "Lang2", "l2")

        val useCase = TranslateUseCase(
                sourceLanguage,
                targetLanguage,
                "text",
                MockApiService()
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
    }

}