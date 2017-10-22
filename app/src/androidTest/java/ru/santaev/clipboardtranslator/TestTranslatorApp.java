package ru.santaev.clipboardtranslator;


import io.reactivex.Single;
import ru.santaev.clipboardtranslator.api.TranslateRequest;
import ru.santaev.clipboardtranslator.api.TranslateResponse;
import ru.santaev.clipboardtranslator.model.IDataModel;

public class TestTranslatorApp extends TranslatorApp{

    @Override
    protected IDataModel buildDataModel() {
        return new IDataModel() {
            @Override
            public Single<TranslateResponse> translate(TranslateRequest request) {
                return null;
            }
        };
    }
}
