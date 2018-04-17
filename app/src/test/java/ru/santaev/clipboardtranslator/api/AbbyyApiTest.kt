package ru.santaev.clipboardtranslator.api

import com.example.santaev.domain.api.TranslateRequestDto
import com.example.santaev.domain.dto.LanguageDto
import org.junit.Test
import ru.santaev.clipboardtranslator.api.abbyy.AbbyyApiService
import ru.santaev.clipboardtranslator.api.abbyy.IAbbyyApiTokenKeeper
import java.util.concurrent.TimeUnit

class AbbyyApiTest {

    var token = ""

    val abbyyTokenKeeper = object : IAbbyyApiTokenKeeper {

        override fun saveToken(token: String) {
            this@AbbyyApiTest.token = token
        }

        override fun getToken(): String? = token

    }

    @Test
    fun testGetToken() {
        val api = AbbyyApiService(abbyyTokenKeeper)
        val testObserver = api
                .authenticate()
                .test()
        testObserver.await(5, TimeUnit.SECONDS)
        val response = testObserver
                .assertComplete()
                .assertValueCount(1)
                .values()
                .first()
        println("Token = ${response.token}")
    }

    @Test
    fun testTranslate() {
        val api = AbbyyApiService(abbyyTokenKeeper)
        token = ""

        val testObserver = api.translate(TranslateRequestDto(
                originText = "Test",
                originLang = LanguageDto(0, "", ""),
                targetLang = LanguageDto(0, "", "")
        )).test()
        testObserver.await(5, TimeUnit.SECONDS)
        testObserver.assertComplete()
        testObserver.assertValueCount(1)

        val response = testObserver.values().first()

        println("Response = ${response.text.first()}")
    }
}