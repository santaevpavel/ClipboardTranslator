package com.example.santaev.domain.api

import com.example.santaev.domain.dto.LanguageDto

data class LanguagesResponseDto(
        var directions: List<String>,
        var languages: List<LanguageDto>
)
