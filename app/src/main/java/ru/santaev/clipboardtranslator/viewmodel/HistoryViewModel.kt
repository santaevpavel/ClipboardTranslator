package ru.santaev.clipboardtranslator.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import ru.santaev.clipboardtranslator.db.entity.Translation
import ru.santaev.clipboardtranslator.model.IHistoryDataModel

class HistoryViewModel(private val dataModel: IHistoryDataModel) : ViewModel() {

    var history: LiveData<List<Translation>> = MutableLiveData()
        private set

    init {
        loadHistory()
    }

    private fun loadHistory() {
        dataModel.translationHistory
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ (history as MutableLiveData).setValue(it) })
    }

    override fun onCleared() {
        super.onCleared()
    }

    fun onClickedItem(translation: Translation) {
    }

    fun removeItem(translation: Translation) {
        dataModel.removeTranslation(translation)
    }

    fun clearHistory() {
        dataModel.deleteAll()
    }
}
