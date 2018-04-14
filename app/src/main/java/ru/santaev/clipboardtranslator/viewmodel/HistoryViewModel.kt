package ru.santaev.clipboardtranslator.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.santaev.domain.dto.TranslationDto
import com.example.santaev.domain.factory.UseCaseFactory
import ru.santaev.clipboardtranslator.util.RxHelper

class HistoryViewModel : ViewModel() {

    var history: LiveData<List<TranslationDto>> = MutableLiveData()
        private set

    init {
        loadHistory()
    }

    private fun loadHistory() {
        UseCaseFactory
                .instance
                .getGetHistoryUseCase()
                .execute()
                .compose(RxHelper.getFlowableTransformer())
                .subscribe({ (history as MutableLiveData).setValue(it) })
    }

    @Suppress("UNUSED_PARAMETER")
    fun onClickedItem(translation: TranslationDto) {
    }

    fun removeItem(translation: TranslationDto) {
        UseCaseFactory
                .instance
                .getDeleteHistoryUseCase()
                .delete(translation)
                .compose(RxHelper.getCompletableTransformer())
                .subscribe()
    }

    fun clearHistory() {
        UseCaseFactory
                .instance
                .getDeleteHistoryUseCase()
                .deleteAll()
                .compose(RxHelper.getCompletableTransformer())
                .subscribe()
    }
}
