package com.example.santaev.domain.usecase

import com.example.santaev.domain.api.IApiService
import com.example.santaev.domain.api.TranslateRequestDto
import com.example.santaev.domain.dto.LanguageDto
import com.example.santaev.domain.dto.TranslationDto
import com.example.santaev.domain.repository.ITranslationRepository
import io.reactivex.Single

class TranslateUseCase(
        private val sourceLanguage: LanguageDto,
        private val targetLanguage: LanguageDto,
        private val text: String,
        private var apiService: IApiService,
        private var translationRepository: ITranslationRepository
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
                    val targetText = response.text.firstOrNull() ?: ""
                    translationRepository.insert(
                            TranslationDto(
                                    id = 0,
                                    sourceLangCode = sourceLanguage.code,
                                    targetLangCode = targetLanguage.code,
                                    sourceText = text,
                                    targetText = targetText
                            )
                    ).subscribe()
                    Response.Success(
                            sourceText = text,
                            targetText = targetText
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