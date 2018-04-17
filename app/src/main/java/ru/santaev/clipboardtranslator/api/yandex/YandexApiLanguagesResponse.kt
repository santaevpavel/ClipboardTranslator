package ru.santaev.clipboardtranslator.api.yandex

import com.google.gson.annotations.SerializedName


class YandexApiLanguagesResponse {

    @SerializedName("dirs")
    var directions: List<String>? = null
    @SerializedName("langs")
    var languages: Map<String, String>? = null
}
