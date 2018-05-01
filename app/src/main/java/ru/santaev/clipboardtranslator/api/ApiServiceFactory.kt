package ru.santaev.clipboardtranslator.api

import android.annotation.SuppressLint
import com.example.santaev.domain.api.IApiService
import okhttp3.OkHttpClient
import ru.santaev.clipboardtranslator.BuildConfig
import ru.santaev.clipboardtranslator.api.abbyy.AbbyyApiService
import ru.santaev.clipboardtranslator.api.abbyy.IAbbyyApiTokenKeeper
import ru.santaev.clipboardtranslator.api.yandex.YandexApiService
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class ApiServiceFactory {

    fun getYandexApiService(): IApiService = YandexApiService.create(apiKey = BuildConfig.YANDEX_TRANSLATE_API_KEY)

    fun getAbbyyApiService(): IApiService {
        return AbbyyApiService.create(
                client = OkHttpClient.Builder()
                        .hostnameVerifier { _, _ -> true }
                        .overrideSslSocketFactory()
                        .build(),
                abbyyApiTokenKeeper = ImMemoryKeeper(),
                apiKey = BuildConfig.ABBYY_API_KEY
        )
    }

    private class ImMemoryKeeper : IAbbyyApiTokenKeeper {

        private var token: String? = null

        override fun saveToken(token: String) {
            this.token = token
        }

        override fun getToken(): String? = token

    }


    companion object {

        val instance by lazy { ApiServiceFactory() }
    }
}

private fun OkHttpClient.Builder.overrideSslSocketFactory(): OkHttpClient.Builder {
    val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {

        override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()

        @SuppressLint("TrustAllX509TrustManager")
        @Throws(CertificateException::class)
        override fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {
        }

        @SuppressLint("TrustAllX509TrustManager")
        @Throws(CertificateException::class)
        override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {
        }
    })
    val sslContext = SSLContext.getInstance("SSL")
    sslContext.init(null, trustAllCerts, java.security.SecureRandom())

    val sslSocketFactory = sslContext.socketFactory
    sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
    return this
}