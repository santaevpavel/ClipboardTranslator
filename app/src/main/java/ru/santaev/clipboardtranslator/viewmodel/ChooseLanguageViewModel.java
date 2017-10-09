package ru.santaev.clipboardtranslator.viewmodel;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import ru.santaev.clipboardtranslator.db.entity.Language;
import ru.santaev.clipboardtranslator.model.IDataModel;

public class ChooseLanguageViewModel extends ViewModel {

    private MutableLiveData<List<Language>> languages = new MutableLiveData<>();
    private IDataModel dataModel;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public ChooseLanguageViewModel(IDataModel dataModel) {
        this.dataModel = dataModel;
        this.languages.setValue(new ArrayList<>());

        compositeDisposable.add(dataModel.getLanguages()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onLanguagesChanged));
    }

    private void onLanguagesChanged(List<Language> languages) {
        this.languages.postValue(languages);
    }

    public LiveData<List<Language>> getLanguages() {
        return languages;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
