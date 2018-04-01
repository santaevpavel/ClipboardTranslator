package com.example.santaev.domain.usecase

import com.example.santaev.domain.dto.TranslationDto
import com.example.santaev.domain.repository.ITranslationRepository

class DeleteHistoryUseCase(
        private var translationRepository: ITranslationRepository
) {

    fun delete(translation: TranslationDto) = translationRepository.delete(translation)

    fun deleteAll() = translationRepository.deleteAll()
}