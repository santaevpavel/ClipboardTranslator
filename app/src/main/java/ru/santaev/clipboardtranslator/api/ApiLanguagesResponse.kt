package ru.santaev.clipboardtranslator.api

import com.google.gson.annotations.SerializedName


class ApiLanguagesResponse {

    @SerializedName("dirs")
    var directions: List<String>? = null
    @SerializedName("langs")
    var languages: Map<String, String>? = null
}
