package ru.santaev.clipboardtranslator.api.yandex


import io.reactivex.Single
import retrofit2.http.*

interface YandexApi {

    @FormUrlEncoded
    @POST("api/v1.5/tr.json/translate")
    fun translate(@Field("text") text: String,
                  @Field("lang") lang: String,
                  @Field("key") apiKey: String): Single<YandexApiTranslateResponse>

    @GET("api/v1.5/tr.json/getLangs")
    fun getLangs(@Query("key") apiKey: String,
                 @Query("ui") uiLang: String): Single<YandexApiLanguagesResponse>

    companion object {

        val SERVER_URL = "https://translate.yandex.net/"
    }
}
