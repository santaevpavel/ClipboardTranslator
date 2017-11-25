package ru.santaev.clipboardtranslator.api

import io.reactivex.Single

interface IApiService {

    fun getLanguages(): Single<LanguagesResponse>

    fun translate(request: TranslateRequest): Single<TranslateResponse>
}
