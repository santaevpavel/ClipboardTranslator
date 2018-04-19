package ru.santaev.clipboardtranslator.api

import com.example.santaev.domain.api.TranslateRequestDto
import com.example.santaev.domain.dto.LanguageDto
import okhttp3.OkHttpClient
import org.junit.Test
import ru.santaev.clipboardtranslator.api.abbyy.AbbyyApiService
import ru.santaev.clipboardtranslator.api.abbyy.IAbbyyApiTokenKeeper
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class AbbyyApiTest {

    private var token = ""
    private val abbyyTokenKeeper = object : IAbbyyApiTokenKeeper {

        override fun saveToken(token: String) {
            this@AbbyyApiTest.token = token
        }

        override fun getToken(): String? = token

    }
    private val okHttpClient = OkHttpClient
            .Builder()
            .hostnameVerifier { _, _ -> true }
            .overrideSslSocketFactory()
            .build()

    @Test
    fun testGetToken() {
        val api = AbbyyApiService(okHttpClient, abbyyTokenKeeper)
        val testObserver = api
                .authenticate()
                .test()
        testObserver.await(5, TimeUnit.SECONDS)
        val response = testObserver
                .assertComplete()
                .assertValueCount(1)
                .values()
                .first()
        println("Token = ${response.token}")
    }

    @Test
    fun testTranslate() {
        val api = AbbyyApiService(okHttpClient, abbyyTokenKeeper)
        token = ""

        val testObserver = api.translate(TranslateRequestDto(
                originText = "Test",
                originLang = LanguageDto(0, "", ""),
                targetLang = LanguageDto(0, "", "")
        )).test()
        testObserver.await(5, TimeUnit.SECONDS)
        testObserver.assertComplete()
        testObserver.assertValueCount(1)

        val response = testObserver.values().first()

        println("Response = ${response.text.first()}")
    }

}

private fun OkHttpClient.Builder.overrideSslSocketFactory(): OkHttpClient.Builder {
    val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {

        override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()

        @Throws(CertificateException::class)
        override fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {
        }

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