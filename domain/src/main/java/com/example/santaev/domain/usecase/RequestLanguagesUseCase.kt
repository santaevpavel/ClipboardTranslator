package com.example.santaev.domain.usecase

import com.example.santaev.domain.repository.ILanguageRepository

class RequestLanguagesUseCase(
        private var languageRepository: ILanguageRepository
) {

    fun execute() = languageRepository.requestLanguages()
}
