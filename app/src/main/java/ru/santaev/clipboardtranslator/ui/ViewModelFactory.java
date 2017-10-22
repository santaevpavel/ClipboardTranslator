package ru.santaev.clipboardtranslator.ui;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import ru.santaev.clipboardtranslator.model.IDataModel;
import ru.santaev.clipboardtranslator.viewmodel.ChooseLanguageViewModel;
import ru.santaev.clipboardtranslator.viewmodel.TranslateViewModel;

class ViewModelFactory implements ViewModelProvider.Factory {

    private IDataModel dataModel;

    public ViewModelFactory(IDataModel dataModel) {
        this.dataModel = dataModel;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.equals(TranslateViewModel.class)) {
            return (T) new TranslateViewModel(dataModel);
        } else if (modelClass.equals(ChooseLanguageViewModel.class)) {
            return (T) new ChooseLanguageViewModel(dataModel);
        } else {
            return null;
        }
    }
}
