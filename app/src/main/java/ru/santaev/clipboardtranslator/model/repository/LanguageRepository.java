package ru.santaev.clipboardtranslator.model.repository;

import android.arch.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import ru.santaev.clipboardtranslator.api.IApiService;
import ru.santaev.clipboardtranslator.api.LanguagesResponse;
import ru.santaev.clipboardtranslator.db.AppDatabase;
import ru.santaev.clipboardtranslator.db.dao.LanguageDao;
import ru.santaev.clipboardtranslator.db.entity.Language;

public class LanguageRepository {

    private IApiService apiService;
    private LanguageDao languageDao;

    private LiveData<List<Language>> languages;

    public LanguageRepository(IApiService apiService) {
        this.apiService = apiService;
        languageDao = AppDatabase.getInstance().getLanguageDao();
    }

    public LiveData<List<Language>> getLanguages() {
        loadLanguages();
        if (languages == null) {
            languages = languageDao.getLanguages();
        }
        return languages;
    }

    private void loadLanguages(){
        Single.fromCallable(() -> apiService.getLanguages())
                .subscribeOn(Schedulers.io())
                .subscribe(this::onLoadLanguages, Throwable::printStackTrace);
    }

    private void onLoadLanguages(LanguagesResponse languagesResponse) {
        Map<String, String> languagesRaw = languagesResponse.getLanguages();
        ArrayList<Language> languages = new ArrayList<>();
        for (String code : languagesRaw.keySet()) {
            languages.add(new Language(0, code, languagesRaw.get(code)));
        }
        languageDao.insertAll(languages);
    }

}
