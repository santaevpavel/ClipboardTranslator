package ru.santaev.clipboardtranslator.api


import com.google.gson.annotations.SerializedName
import java.util.*


class ApiTranslateResponse {

    @SerializedName("code")
    var code: Int = 0
    @SerializedName("lang")
    var lang: String? = null
    @SerializedName("text")
    var text: ArrayList<String>? = null
}
