package ru.santaev.clipboardtranslator.model;


import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import ru.santaev.clipboardtranslator.api.IApiService;
import ru.santaev.clipboardtranslator.api.TranslateRequest;
import ru.santaev.clipboardtranslator.db.AppDatabase;
import ru.santaev.clipboardtranslator.db.entity.Translation;

public class DataModel implements IDataModel{

    private IApiService apiService;

    private Translation lastTranslation;

    public DataModel(IApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public Single<TranslateResponse> translate(Language originLang, Language targetLang,
                                               String originText) {

        TranslateRequest request = new TranslateRequest(originText, originLang.getCode(),
                targetLang.getCode());

        return apiService.translate(request)
                .map(response -> {
                    String targetText = response.getText().size() == 0
                            ? "" : response.getText().get(0);
                    saveTransition(originText, targetText, originLang, targetLang);
                    return new TranslateResponse(targetText);
                }).subscribeOn(Schedulers.io());
    }

    private void saveTransition(String finalOriginText, String targetText,
                                Language finalOriginLang, Language finalTargetLang) {

        Translation newTranslation = new Translation(finalOriginLang.getCode(),
                finalTargetLang.getCode(), finalOriginText, targetText);

        if (lastTranslation != null
                && finalOriginText.contains(lastTranslation.getTextSource())){

            Translation savedTranslation = AppDatabase.getInstance()
                    .getTranslationDao().getTranslationSync(lastTranslation.getId());
            if (savedTranslation != null) {
                newTranslation.setId(lastTranslation.getId());
            }
        }
        lastTranslation = newTranslation;

        long id = AppDatabase.getInstance().getTranslationDao().insert(lastTranslation);
        lastTranslation.setId(id);
    }
}
