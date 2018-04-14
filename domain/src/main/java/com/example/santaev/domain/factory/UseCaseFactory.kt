package com.example.santaev.domain.factory

import com.example.santaev.domain.dto.LanguageDto
import com.example.santaev.domain.usecase.*

class UseCaseFactory(
        private val repositoryFactory: RepositoryFactory,
        private val gatewayFactory: IGatewayFactory
) {

    fun getTranslateFactory(
            sourceLanguage: LanguageDto,
            targetLanguage: LanguageDto,
            text: String
    ): TranslateUseCase {
        return TranslateUseCase(
                sourceLanguage = sourceLanguage,
                targetLanguage = targetLanguage,
                text = text,
                apiService = gatewayFactory.getApiService(),
                translationRepository = repositoryFactory.getTranslationRepository()
        )
    }

    fun getGetHistoryUseCase() = GetHistoryUseCase(repositoryFactory.getTranslationRepository())

    fun getGetLanguagesUseCase() = GetLanguagesUseCase(repositoryFactory.getLanguageRepository())

    fun getDeleteHistoryUseCase() = DeleteHistoryUseCase(repositoryFactory.getTranslationRepository())

    fun getRequestLanguagesUseCase() = RequestLanguagesUseCase(repositoryFactory.getLanguageRepository())

    companion object {

        val instance by lazy {
            UseCaseFactory(
                    RepositoryFactory.instance,
                    IGatewayFactory.instance
            )
        }
    }
}