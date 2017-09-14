package ru.santaev.clipboardtranslator.api;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import ru.santaev.clipboardtranslator.BuildConfig;

public interface YandexApi {

    String API_KEY = BuildConfig.YANDEX_TRANSLATE_API_KEY;
    String SERVER_URL = "https://translate.yandex.net/";

    @FormUrlEncoded
    @POST("api/v1.5/tr.json/translate")
    Call<TranslateResponse> translate(@Field("text") String text,
                                      @Field("lang") String lang,
                                      @Field("key") String apiKey);

    @FormUrlEncoded
    @POST("api/v1.5/tr.json/translate")
    Call<TranslateResponse> getLangs(@Field("text") String text);
}
