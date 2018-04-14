package com.example.santaev.domain.usecase

import com.example.santaev.domain.repository.ITranslationRepository

class GetHistoryUseCase(
        private var translationRepository: ITranslationRepository
) {

    fun execute() = translationRepository.getTranslations()
}