package ru.santaev.clipboardtranslator.api.abbyy


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
import retrofit2.converter.scalars.ScalarsConverterFactory
import ru.santaev.clipboardtranslator.api.abbyy.AbbyyApi.Companion.API_KEY
import ru.santaev.clipboardtranslator.api.abbyy.AbbyyApi.Companion.SERVER_URL
import java.io.IOException
import java.net.SocketTimeoutException

class AbbyyApiService(
        client: OkHttpClient,
        private val abbyyApiTokenKeeper: IAbbyyApiTokenKeeper
) : IApiService {

    private val api: AbbyyApi

    init {
        val retrofit = Retrofit.Builder()
                .baseUrl(SERVER_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build()
        api = retrofit.create(AbbyyApi::class.java)
    }

    fun authenticate(): Single<AbbyyApiAuthenticateResponse> {
        return api
                .authenticate(API_KEY.toBasicAuth())
                .map { AbbyyApiAuthenticateResponse(it) }
                .compose(getApiTransformer())
                .map { response ->
                    abbyyApiTokenKeeper.saveToken(response.token)
                    response
                }
    }

    override fun translate(request: TranslateRequestDto): Single<TranslateResponseDto> {
        val storedToken = abbyyApiTokenKeeper.getToken()
        val authSingle = if (storedToken?.isNotEmpty() == true) Single.just(storedToken) else authenticate().map { it.token }
        return authSingle.flatMap { token ->
            api.translate(
                    token = token.toBearerAuth(),
                    text = request.originText,
                    srcLang = request.originLang.code,
                    dstLang = request.targetLang.code
            )
                    .compose(getApiTransformer())
                    .map { response ->
                        val text = checkNotNull(response.translation.translation)
                        TranslateResponseDto(0, request.targetLang, arrayListOf(text))
                    }
        }
    }


    override fun getLanguages(): Single<LanguagesResponseDto> = Single.just(
            LanguagesResponseDto(
                    directions = listOf(),
                    languages = listOf(
                            LanguageDto(0, "Русский", "1049"),
                            LanguageDto(0, "Английский", "1033")
                    )
            )
    )

    private fun <T> getApiTransformer(): SingleTransformer<T, T> {
        return SingleTransformer { upstream ->
            upstream.onErrorResumeNext({ throwable ->
                throwable.printStackTrace()
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

    private fun String.toBearerAuth() = "Bearer $this"

    private fun String.toBasicAuth() = "Basic $this"
}
