package ru.santaev.clipboardtranslator.api.abbyy


import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface AbbyyApi {

    @POST("api/v1.1/authenticate")
    fun authenticate(@Header("Authorization") authorization: String): Single<String>

    @GET("api/v1/Minicard")
    fun translate(
            @Header("Authorization") token: String,
            @Query("text") text: String,
            @Query("srcLang") srcLang: String,
            @Query("dstLang") dstLang: String
    ): Single<AbbyyApiTranslateResponse>

    companion object {

        val SERVER_URL = "https://developers.lingvolive.com/"
    }
}
