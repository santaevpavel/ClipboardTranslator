package ru.santaev.clipboardtranslator.model;


import io.reactivex.Single;
import ru.santaev.clipboardtranslator.api.IApiService;
import ru.santaev.clipboardtranslator.api.TranslateRequest;
import ru.santaev.clipboardtranslator.api.TranslateResponse;

public class DataModel implements IDataModel{

    private IApiService apiService;

    public DataModel(IApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public Single<TranslateResponse> translate(TranslateRequest request) {
        return apiService.translate(request);
    }


}
