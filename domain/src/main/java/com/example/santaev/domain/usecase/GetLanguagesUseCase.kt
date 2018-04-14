package com.example.santaev.domain.usecase

import com.example.santaev.domain.repository.ILanguageRepository

class GetLanguagesUseCase(
        private var languageRepository: ILanguageRepository
) {

    fun execute() = languageRepository.getLanguages()
}
