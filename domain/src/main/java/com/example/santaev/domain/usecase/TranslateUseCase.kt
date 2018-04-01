package com.example.santaev.domain.usecase

import com.example.santaev.domain.api.IApiService
import com.example.santaev.domain.api.TranslateRequestDto
import com.example.santaev.domain.dto.LanguageDto
import io.reactivex.Single

class TranslateUseCase(
        private val sourceLanguage: LanguageDto,
        private val targetLanguage: LanguageDto,
        private val text: String,
        private var apiService: IApiService
) {

    fun execute(): Single<out Response> {
        return apiService.translate(
                TranslateRequestDto(
                        text,
                        sourceLanguage,
                        targetLanguage
                )
        )
                .map { response ->
                    Response.Success(
                            text,
                            response.text.firstOrNull() ?: ""
                    ) as Response
                }
                .onErrorResumeNext { Single.just(Response.Error()) }
    }

    sealed class Response {

        class Error : Response()

        data class Success(
                val sourceText: String,
                val targetText: String
        ) : Response()
    }

}