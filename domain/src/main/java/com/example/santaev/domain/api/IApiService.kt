package com.example.santaev.domain.api

import io.reactivex.Single

interface IApiService {

    fun getLanguages(): Single<LanguagesResponseDto>

    fun translate(request: TranslateRequestDto): Single<TranslateResponseDto>
}
