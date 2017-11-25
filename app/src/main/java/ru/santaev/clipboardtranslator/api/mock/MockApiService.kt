package ru.santaev.clipboardtranslator.api.mock

import io.reactivex.Single
import ru.santaev.clipboardtranslator.api.IApiService
import ru.santaev.clipboardtranslator.api.LanguagesResponse
import ru.santaev.clipboardtranslator.api.TranslateRequest
import ru.santaev.clipboardtranslator.api.TranslateResponse

class MockApiService : IApiService {

    override fun translate(request: TranslateRequest): Single<TranslateResponse> {
        val response = TranslateResponse()
        val postfix = "${request.originLang} ${request.targetLang}"
        response.code = 200
        response.lang = "lang"
        response.text = arrayListOf(when (request.originText) {
            "текст" -> "text " + postfix
            "текст 1" -> "text 1 " + postfix
            "текст 2" -> "text 2 " + postfix
            "текст 3" -> "text 3 " + postfix
            else -> "translated text '${request.originText} 'from ${request.originLang} to ${request.targetLang}"
        })
        return Single.just(response)
    }

    override fun getLanguages(): Single<LanguagesResponse> {
        val response = LanguagesResponse()
        response.directions = arrayListOf()
        response.languages = mapOf(
                Pair("java", "джава"),
                Pair("kotlin", "котлин"),
                Pair("c#", "си шарп"),
                Pair("c++", "си плюс плюс")
        )
        return Single.just(response)
    }
}