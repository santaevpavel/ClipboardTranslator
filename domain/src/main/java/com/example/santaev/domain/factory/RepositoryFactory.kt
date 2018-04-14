package com.example.santaev.domain.factory

import com.example.santaev.domain.repository.ILanguageRepository
import com.example.santaev.domain.repository.ITranslationRepository
import com.example.santaev.domain.repository.LanguageRepository
import com.example.santaev.domain.repository.TranslationRepository

class RepositoryFactory(
        private val gatewayFactory: IGatewayFactory
) {

    private val languageRepositoryInstance: ILanguageRepository by lazy {
        LanguageRepository(
                gatewayFactory.getLanguageDao(),
                gatewayFactory.getApiService()

        )
    }
    private val translationRepositoryInstance: ITranslationRepository by lazy {
        TranslationRepository(
                gatewayFactory.getTranslationDao()
        )
    }

    fun getLanguageRepository(): ILanguageRepository = languageRepositoryInstance

    fun getTranslationRepository(): ITranslationRepository = translationRepositoryInstance

    companion object {

        val instance by lazy { RepositoryFactory(IGatewayFactory.instance) }
    }
}