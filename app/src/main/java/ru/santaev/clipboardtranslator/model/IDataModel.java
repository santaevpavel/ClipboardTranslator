package ru.santaev.clipboardtranslator.model;


import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;
import ru.santaev.clipboardtranslator.db.entity.Language;

public interface IDataModel {

    class TranslateResponse{

        public String targetText;

        public TranslateResponse(String targetText) {
            this.targetText = targetText;
        }
    }

    Single<TranslateResponse> translate(Language origin, Language target, String text);

    Flowable<List<Language>> getLanguages();

}
