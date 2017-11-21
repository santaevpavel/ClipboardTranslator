package ru.santaev.clipboardtranslator.api


class TranslateRequest(
        var originText: String,
        var originLang: String,
        var targetLang: String
) {
    var lang: String = String.format("%s-%s", originLang, targetLang)
}
