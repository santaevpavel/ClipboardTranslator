package ru.santaev.clipboardtranslator.api

import com.example.santaev.domain.api.TranslateRequestDto
import com.example.santaev.domain.dto.LanguageDto
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import ru.santaev.clipboardtranslator.BuildConfig
import ru.santaev.clipboardtranslator.api.yandex.YandexApiService
import java.util.concurrent.TimeUnit

class YandexApiTest {

    private lateinit var api: YandexApiService

    @Before
    fun setUp() {
        api = YandexApiService.create(BuildConfig.YANDEX_TRANSLATE_API_KEY)
    }

    @Test
    fun testGetLanguages() {
        val testObserver = api
                .getLanguages()
                .test()
        testObserver.await(5, TimeUnit.SECONDS)
        val response = testObserver
                .assertComplete()
                .assertValueCount(1)
                .values()
                .first()
        println("Languages = ${response.languages}")

        assertNotNull(response.languages)
        assertNotNull(response.directions)
    }

    @Test
    fun testTranslate() {
        val testObserver = api.translate(TranslateRequestDto(
                originText = "Development",
                originLang = LanguageDto(0, "", "en"),
                targetLang = LanguageDto(0, "", "ru")
        )).test()
        testObserver.await(5, TimeUnit.SECONDS)
        testObserver.assertComplete()
        testObserver.assertValueCount(1)

        val response = testObserver.values().first()
        println("Response = ${response.text.first()}")

        assertNotNull(response.text)
    }

}