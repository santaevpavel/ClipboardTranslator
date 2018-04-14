package com.example.santaev.domain.dto


data class TranslateDto(
        val sourceLangCode: String,
        val targetLangCode: String,
        val sourceText: String,
        val targetText: String
)