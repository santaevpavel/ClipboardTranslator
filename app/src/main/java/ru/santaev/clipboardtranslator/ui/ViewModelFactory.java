package ru.santaev.clipboardtranslator.ui;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import ru.santaev.clipboardtranslator.model.IDataModel;
import ru.santaev.clipboardtranslator.model.IHistoryDataModel;
import ru.santaev.clipboardtranslator.viewmodel.ChooseLanguageViewModel;
import ru.santaev.clipboardtranslator.viewmodel.HistoryViewModel;
import ru.santaev.clipboardtranslator.viewmodel.TranslateViewModel;

class ViewModelFactory implements ViewModelProvider.Factory {

    private IDataModel dataModel;
    private IHistoryDataModel historyDataModel;

    public ViewModelFactory(IDataModel dataModel) {
        this.dataModel = dataModel;
    }

    public ViewModelFactory(IHistoryDataModel historyDataModel) {
        this.historyDataModel = historyDataModel;
    }

    @NonNull
    @SuppressWarnings("unchecked")
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.equals(TranslateViewModel.class)) {
            return (T) new TranslateViewModel(dataModel);
        } else if (modelClass.equals(ChooseLanguageViewModel.class)) {
            return (T) new ChooseLanguageViewModel(dataModel);
        } else if (modelClass.equals(HistoryViewModel.class)) {
            return (T) new HistoryViewModel(historyDataModel);
        } else throw new IllegalArgumentException("Incorrect view model class: " + modelClass);
    }
}
