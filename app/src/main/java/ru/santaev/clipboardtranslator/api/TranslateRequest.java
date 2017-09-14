package ru.santaev.clipboardtranslator.api;


public class TranslateRequest {

    public String originText;
    public String lang;

    public TranslateRequest(String originText, String originLang, String targetLang) {
        this.originText = originText;
        this.lang = String.format("%s-%s", originLang, targetLang);
    }
}
