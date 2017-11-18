package ru.santaev.clipboardtranslator.api


class TranslateRequest(
        var originText: String,
        originLang: String,
        targetLang: String
) {
    var lang: String = String.format("%s-%s", originLang, targetLang)
}
