package ru.santaev.clipboardtranslator;


import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;
import ru.santaev.clipboardtranslator.db.entity.Language;
import ru.santaev.clipboardtranslator.model.IDataModel;

public class TestTranslatorApp extends TranslatorApp{

    @Override
    protected IDataModel buildDataModel() {
        return new IDataModel() {
            @Override
            public Single<TranslateResponse> translate(Language origin, Language target, String text) {
                return null;
            }

            @Override
            public Flowable<List<Language>> getLanguages() {
                return null;
            }

        };
    }
}
