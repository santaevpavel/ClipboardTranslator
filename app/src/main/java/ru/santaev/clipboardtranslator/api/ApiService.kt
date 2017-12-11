package ru.santaev.clipboardtranslator.api


import io.reactivex.Single
import io.reactivex.SingleTransformer
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.santaev.clipboardtranslator.api.YandexApi.Companion.API_KEY
import ru.santaev.clipboardtranslator.api.YandexApi.Companion.SERVER_URL
import java.io.IOException
import java.net.SocketTimeoutException
import java.util.*

class ApiService : IApiService {

    private val api: YandexApi

    init {
        val okHttpClient = OkHttpClient.Builder().build()
        val retrofit = Retrofit.Builder()
                .baseUrl(SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()
        api = retrofit.create(YandexApi::class.java)
    }

    override fun translate(request: TranslateRequest): Single<TranslateResponse> {
        return api.translate(request.originText, request.lang, API_KEY)
                .compose(getApiTransformer())
    }

    override fun getLanguages(): Single<LanguagesResponse> {
        return api.getLangs(API_KEY, Locale.getDefault().language)
                .compose(getApiTransformer())
    }

    private fun <T> getApiTransformer(): SingleTransformer<T, T> {
        return SingleTransformer { upstream ->
            upstream.onErrorResumeNext({ throwable ->
                if (throwable is SocketTimeoutException) {
                    Single.error<T>(throwApiError("Timeout"))
                } else if (throwable is IOException) {
                    Single.error<T>(throwApiError("Connection error"))
                } else {
                    Single.error<T>(throwApiError("Request failed"))
                }
            })
        }
    }

    private fun throwApiError(msg: String): RuntimeException {
        return RuntimeException(msg)
    }

}
