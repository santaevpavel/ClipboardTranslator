package com.example.santaev.domain.api

import com.example.santaev.domain.dto.LanguageDto
import java.util.*

data class TranslateResponseDto(
        var code: Int,
        var lang: LanguageDto,
        var text: ArrayList<String>
)
