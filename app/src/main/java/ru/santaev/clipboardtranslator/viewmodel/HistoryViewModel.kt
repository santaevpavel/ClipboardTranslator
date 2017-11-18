package ru.santaev.clipboardtranslator.viewmodel


import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.functions.Consumer

import ru.santaev.clipboardtranslator.db.AppDatabase
import ru.santaev.clipboardtranslator.db.dao.TranslationDao
import ru.santaev.clipboardtranslator.db.entity.Translation
import ru.santaev.clipboardtranslator.util.RxHelper
import java.util.concurrent.Callable

class HistoryViewModel : ViewModel() {

    var history: LiveData<List<Translation>> = MutableLiveData()
        private set

    private var translationDao: TranslationDao = AppDatabase.getInstance().translationDao

    init {
        loadHistory()
    }

    private fun loadHistory() {
        history = translationDao.translations
    }

    override fun onCleared() {
        super.onCleared()
    }

    fun onClickedItem(translation: Translation) {
    }

    fun removeItem(translation: Translation) {
        RxHelper.runOnIoThread<Any>(Callable {
            translationDao.delete(translation)
            null
        }, Consumer { it.printStackTrace() }, null, null)
    }

    fun clearHistory() {
        RxHelper.runOnIoThread<Any>(Callable {
            translationDao.deleteAll()
            null
        }, Consumer { it.printStackTrace() }, null, null)
    }
}
