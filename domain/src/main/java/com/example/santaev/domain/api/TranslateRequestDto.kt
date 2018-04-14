package com.example.santaev.domain.api

import com.example.santaev.domain.dto.LanguageDto


class TranslateRequestDto(
        var originText: String,
        var originLang: LanguageDto,
        var targetLang: LanguageDto
)
