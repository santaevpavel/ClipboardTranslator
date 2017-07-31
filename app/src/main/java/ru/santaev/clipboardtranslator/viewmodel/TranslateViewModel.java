package ru.santaev.clipboardtranslator.viewmodel;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.Handler;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.santaev.clipboardtranslator.api.ApiService;
import ru.santaev.clipboardtranslator.api.TranslateRequest;
import ru.santaev.clipboardtranslator.db.AppDatabase;
import ru.santaev.clipboardtranslator.db.entity.Translation;
import ru.santaev.clipboardtranslator.model.Language;
import ru.santaev.clipboardtranslator.util.AppPreference;


public class TranslateViewModel extends ViewModel{

    private static final long TRANSLATE_DELAY_MILLIS = TimeUnit.MILLISECONDS.toMillis(500);

    private MutableLiveData<String> translatedText = new MutableLiveData<>();
    private MutableLiveData<Boolean> progress = new MutableLiveData<>();

    private MutableLiveData<Language> originLang = new MutableLiveData<>();
    private MutableLiveData<Language> targetLang = new MutableLiveData<>();

    private Disposable disposable;
    private String originText;
    private Handler handler;
    private Runnable runTranslate;
    private Translation lastTranslation;

    private AppPreference appPreference;

    public TranslateViewModel() {
        translatedText.setValue("");
        progress.setValue(false);

        appPreference = AppPreference.getInstance();

        originLang.setValue(appPreference.getOriginLang());
        targetLang.setValue(appPreference.getTargetLang());
        originText = "";

        handler = new Handler();
    }

    public void onOriginTextChanged(String text){
        originText = text;
        translate();
    }

    public void onOriginLangSelected(Language lang){
        Language oldLang = originLang.getValue();
        originLang.setValue(lang);
        appPreference.setOriginLang(lang);
        if (lang != oldLang){
            translate();
        }
    }

    public void onTargetLangSelected(Language lang){
        Language oldLang = targetLang.getValue();
        targetLang.setValue(lang);
        appPreference.setTargetLang(lang);
        if (lang != oldLang){
            translate();
        }
    }

    public LiveData<String> getTranslatedText(){
        return translatedText;
    }

    public LiveData<Boolean> getProgress(){
        return progress;
    }

    public LiveData<Language> getOriginLang() {
        return originLang;
    }

    public LiveData<Language> getTargetLang() {
        return targetLang;
    }

    private void translate(){
        if (originText.isEmpty()){
            return;
        }
        if (disposable != null){
            disposable.dispose();
        }

        if (runTranslate != null){
            handler.removeCallbacks(runTranslate);
        }

        runTranslate = () -> {
            progress.setValue(true);

            String finalOriginText = originText;
            Language finalOriginLang = originLang.getValue();
            Language finalTargetLang = targetLang.getValue();

            TranslateRequest translateRequest = new TranslateRequest(originText,
                    originLang.getValue().getCode(), targetLang.getValue().getCode());

            disposable = ApiService.getInstance().translate(translateRequest)
                    .map(response -> {
                        String targetText = response.getText().size() == 0
                                ? "" : response.getText().get(0);
                        saveTransition(finalOriginText, targetText, finalOriginLang, finalTargetLang);
                        return response;
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(translateResponse -> {
                        String targetText = translateResponse.getText().size() == 0
                                ? "" : translateResponse.getText().get(0);
                        translatedText.setValue(targetText);
                        progress.setValue(false);
                    }, throwable -> {
                        translatedText.setValue(throwable.getMessage());
                        progress.setValue(false);
                    });
        };

        handler.postDelayed(runTranslate, TRANSLATE_DELAY_MILLIS);
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


    @Override
    protected void onCleared() {
        super.onCleared();
        if (null != disposable){
            disposable.dispose();
        }
        if (runTranslate != null){
            handler.removeCallbacks(runTranslate);
        }
        handler = null;
    }
}
