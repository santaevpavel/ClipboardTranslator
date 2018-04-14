package com.example.santaev.domain.dto


data class TranslationDto(
        val id: Long,
        val sourceLangCode: String,
        val targetLangCode: String,
        val sourceText: String,
        val targetText: String
)