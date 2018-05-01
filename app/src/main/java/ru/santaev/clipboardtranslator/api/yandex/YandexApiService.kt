package ru.santaev.clipboardtranslator.api.yandex


import com.example.santaev.domain.api.IApiService
import com.example.santaev.domain.api.LanguagesResponseDto
import com.example.santaev.domain.api.TranslateRequestDto
import com.example.santaev.domain.api.TranslateResponseDto
import com.example.santaev.domain.dto.LanguageDto
import io.reactivex.Single
import io.reactivex.SingleTransformer
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.santaev.clipboardtranslator.api.yandex.YandexApi.Companion.SERVER_URL
import java.io.IOException
import java.net.SocketTimeoutException
import java.util.*

class YandexApiService(
        private val api: YandexApi,
        private val apiKey: String
) : IApiService {

    override fun translate(request: TranslateRequestDto): Single<TranslateResponseDto> {
        val languagesParam = "${request.originLang.code}-${request.targetLang.code}"
        return api.translate(
                request.originText,
                languagesParam,
                apiKey
        )
                .compose(getApiTransformer())
                .map { response ->
                    val text = checkNotNull(response.text)
                    TranslateResponseDto(0, request.targetLang, text)
                }
    }


    override fun getLanguages(): Single<LanguagesResponseDto> {
        return api.getLangs(
                apiKey,
                Locale.getDefault().language
        ).compose(getApiTransformer())
                .map { response ->
                    val directions = checkNotNull(response.directions)
                    val languagesRaw = checkNotNull(response.languages)
                    val languages = languagesRaw.map { LanguageDto(0, it.key, it.value) }
                    LanguagesResponseDto(directions, languages)
                }
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

    companion object {

        fun create(apiKey: String): YandexApiService {
            val okHttpClient = OkHttpClient.Builder().build()
            val retrofit = Retrofit.Builder()
                    .baseUrl(SERVER_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(okHttpClient)
                    .build()
            return YandexApiService(
                    apiKey = apiKey,
                    api = retrofit.create(YandexApi::class.java)
            )
        }
    }
}
