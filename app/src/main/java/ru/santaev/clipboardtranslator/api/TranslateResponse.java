package ru.santaev.clipboardtranslator.api;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TranslateResponse {

    @SerializedName("code")
    private int code;
    @SerializedName("lang")
    private String lang;
    @SerializedName("text")
    private ArrayList<String> text;

    public int getCode() {
        return code;
    }

    public String getLang() {
        return lang;
    }

    public ArrayList<String> getText() {
        return text;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public void setText(ArrayList<String> text) {
        this.text = text;
    }
}
