package ru.santaev.clipboardtranslator.api;

import io.reactivex.Single;

public interface IApiService {

    Single<TranslateResponse> translate(TranslateRequest request);

    LanguagesResponse getLanguages();
}
