package ru.santaev.clipboardtranslator.api;


import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class LanguagesResponse {

    @SerializedName("dirs")
    private List<String> directions;
    @SerializedName("langs")
    private Map<String, String> languages;

    public List<String> getDirections() {
        return directions;
    }

    public Map<String, String> getLanguages() {
        return languages;
    }
}
