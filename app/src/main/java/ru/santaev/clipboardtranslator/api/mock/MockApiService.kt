package ru.santaev.clipboardtranslator.api.mock

import com.example.santaev.domain.api.IApiService
import com.example.santaev.domain.api.LanguagesResponseDto
import com.example.santaev.domain.api.TranslateRequestDto
import com.example.santaev.domain.api.TranslateResponseDto
import com.example.santaev.domain.dto.LanguageDto
import io.reactivex.Single

class MockApiService : IApiService {

    override fun translate(request: TranslateRequestDto): Single<TranslateResponseDto> {
        val postfix = "${request.originLang} ${request.targetLang}"
        val response = TranslateResponseDto(
                200,
                request.targetLang,
                arrayListOf(when (request.originText) {
                    "текст" -> "text " + postfix
                    "текст 1" -> "text 1 " + postfix
                    "текст 2" -> "text 2 " + postfix
                    "текст 3" -> "text 3 " + postfix
                    else -> "translated text '${request.originText} 'from ${request.originLang} to ${request.targetLang}"
                })
        )
        return Single.just(response)
    }

    override fun getLanguages(): Single<LanguagesResponseDto> {
        val response = LanguagesResponseDto(
                arrayListOf(),
                listOf(
                        LanguageDto(0, "java", "джава"),
                        LanguageDto(0, "kotlin", "котлин"),
                        LanguageDto(0, "c#", "си шарп"),
                        LanguageDto(0, "c++", "си плюс плюс")
                )
        )
        return Single.just(response)
    }
}