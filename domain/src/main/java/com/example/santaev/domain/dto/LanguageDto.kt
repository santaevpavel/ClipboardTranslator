package com.example.santaev.domain.dto

import java.io.Serializable


data class LanguageDto(
        val id: Long,
        val name: String,
        val code: String
) : Serializable