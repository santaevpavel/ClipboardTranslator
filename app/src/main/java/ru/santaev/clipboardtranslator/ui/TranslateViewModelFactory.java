package ru.santaev.clipboardtranslator.ui;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import ru.santaev.clipboardtranslator.model.IDataModel;
import ru.santaev.clipboardtranslator.viewmodel.TranslateViewModel;

class TranslateViewModelFactory implements ViewModelProvider.Factory{

    private IDataModel dataModel;

    public TranslateViewModelFactory(IDataModel dataModel) {
        this.dataModel = dataModel;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.equals(TranslateViewModel.class)) {
            return (T) new TranslateViewModel(dataModel);
        } else {
            return null;
        }
    }
}
