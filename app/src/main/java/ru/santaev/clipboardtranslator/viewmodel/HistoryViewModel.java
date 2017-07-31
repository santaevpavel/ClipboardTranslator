package ru.santaev.clipboardtranslator.viewmodel;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import ru.santaev.clipboardtranslator.TranslatorApp;
import ru.santaev.clipboardtranslator.db.AppDatabase;
import ru.santaev.clipboardtranslator.db.dao.TranslationDao;
import ru.santaev.clipboardtranslator.db.entity.Translation;
import ru.santaev.clipboardtranslator.util.NotificationHelper;
import ru.santaev.clipboardtranslator.util.RxHelper;

public class HistoryViewModel extends ViewModel {

    private TranslationDao translationDao;
    private LiveData<List<Translation>> history = new MutableLiveData<>();

    public HistoryViewModel() {
        translationDao = AppDatabase.getInstance().getTranslationDao();
        loadHistory();
    }

    public LiveData<List<Translation>> getHistory() {
        return history;
    }

    private void loadHistory(){
        history = translationDao.getTranslations();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        translationDao = null;
        history = null;
    }

    public void onClickedItem(Translation translation) {
    }

    public void removeItem(Translation translation) {
        RxHelper.make(() -> {
            translationDao.delete(translation);
            return null;
        }, Throwable::printStackTrace, null, null);
    }
}
