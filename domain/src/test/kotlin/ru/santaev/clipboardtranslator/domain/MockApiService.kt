package ru.santaev.clipboardtranslator.domain

import com.example.santaev.domain.api.IApiService
import com.example.santaev.domain.api.LanguagesResponseDto
import com.example.santaev.domain.api.TranslateRequestDto
import com.example.santaev.domain.api.TranslateResponseDto
import com.example.santaev.domain.dto.LanguageDto
import io.reactivex.Single

class MockApiService : IApiService {

    override fun translate(request: TranslateRequestDto): Single<TranslateResponseDto> {
        val response = TranslateResponseDto(
                200,
                request.targetLang,
                arrayListOf("translated ${request.originText} from ${request.originLang.code} " +
                        "to ${request.targetLang.code}")
        )
        return Single.just(response)
    }

    override fun getLanguages(): Single<LanguagesResponseDto> {
        val response = LanguagesResponseDto(
                arrayListOf(),
                listOf(
                        kotlinLang,
                        javaLang,
                        cSharpLang,
                        cPlusPlusLang
                )
        )
        return Single.just(response)
    }

    companion object {

        val kotlinLang = LanguageDto(0, "kotlin", "kt")
        val javaLang = LanguageDto(0, "java", "java")
        val cSharpLang = LanguageDto(0, "c sharp", "c#")
        val cPlusPlusLang = LanguageDto(0, "c plus plus", "c++")
    }
}