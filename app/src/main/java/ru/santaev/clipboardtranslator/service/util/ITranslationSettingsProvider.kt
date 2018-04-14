package ru.santaev.clipboardtranslator.service.util


import com.example.santaev.domain.dto.LanguageDto

interface ITranslationSettingsProvider {

    var originLang: LanguageDto?

    var targetLang: LanguageDto?
}
