package ru.santaev.clipboardtranslator.api


import io.reactivex.Single
import retrofit2.http.*
import ru.santaev.clipboardtranslator.BuildConfig

interface YandexApi {

    @FormUrlEncoded
    @POST("api/v1.5/tr.json/translate")
    fun translate(@Field("text") text: String,
                  @Field("lang") lang: String,
                  @Field("key") apiKey: String): Single<TranslateResponse>

    @GET("api/v1.5/tr.json/getLangs")
    fun getLangs(@Query("key") apiKey: String,
                 @Query("ui") uiLang: String): Single<LanguagesResponse>

    companion object {

        val API_KEY = BuildConfig.YANDEX_TRANSLATE_API_KEY
        val SERVER_URL = "https://translate.yandex.net/"
    }
}
