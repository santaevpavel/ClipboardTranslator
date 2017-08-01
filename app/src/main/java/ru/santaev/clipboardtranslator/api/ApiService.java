package ru.santaev.clipboardtranslator.api;


import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.Callable;

import io.reactivex.Single;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static ru.santaev.clipboardtranslator.api.YandexApi.API_KEY;
import static ru.santaev.clipboardtranslator.api.YandexApi.SERVER_URL;

public class ApiService {

    private static ApiService instance;
    private final YandexApi api;

    private ApiService() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        api = retrofit.create(YandexApi.class);
    }

    public static ApiService getInstance() {
        if (instance == null) {
            instance = new ApiService();
        }
        return instance;
    }

    public Single<TranslateResponse> translate(TranslateRequest request) {
        return wrapSingle(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Call<TranslateResponse> call = api.translate(request.originText, request.lang, API_KEY);
            return makeRequest(call);
        });
    }

    private <T> T makeRequest(Call<T> httpCall) {
        Response<T> response = null;
        try {
            response = httpCall.execute();
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            throwApiError("Timeout");
        } catch (IOException e) {
            e.printStackTrace();
            throwApiError("Request failed");
        }
        if (response == null || !response.isSuccessful()) {
            throwApiError("Request failed");
        }
        return response.body();
    }

    private void throwApiError(String msg) {
        throw new RuntimeException(msg);
    }

    private <T> Single<T> wrapSingle(Callable<T> func){
        return Single.create(singleSubscriber -> {
            try {
                T t = func.call();
                singleSubscriber.onSuccess(t);
            } catch (Throwable e) {
                if (!singleSubscriber.isDisposed()) {
                    singleSubscriber.onError(e);
                }
            }
        });
    }
}
