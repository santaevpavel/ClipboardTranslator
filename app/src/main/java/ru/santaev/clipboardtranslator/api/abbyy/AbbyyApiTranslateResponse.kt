package ru.santaev.clipboardtranslator.api.abbyy

import com.google.gson.annotations.SerializedName

class AbbyyApiTranslateResponse(
        @SerializedName("Translation") val translation: Translation
) {

    class Translation(
            @SerializedName("Translation") val translation: String
    )
}