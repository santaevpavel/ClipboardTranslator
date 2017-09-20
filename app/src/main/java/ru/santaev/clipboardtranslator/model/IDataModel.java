package ru.santaev.clipboardtranslator.model;


import io.reactivex.Single;

public interface IDataModel {

    public class TranslateResponse{

        public String targetText;

        public TranslateResponse(String targetText) {
            this.targetText = targetText;
        }
    }

    Single<TranslateResponse> translate(Language origin, Language target, String text);

}
