package ru.santaev.clipboardtranslator.model;


import io.reactivex.Single;
import ru.santaev.clipboardtranslator.api.TranslateRequest;
import ru.santaev.clipboardtranslator.api.TranslateResponse;

public interface IDataModel {

    Single<TranslateResponse> translate(TranslateRequest request);

}
