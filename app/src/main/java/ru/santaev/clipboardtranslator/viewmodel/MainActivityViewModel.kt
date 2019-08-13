package ru.santaev.clipboardtranslator.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Log
import com.example.santaev.domain.factory.UseCaseFactory
import com.example.santaev.domain.repository.ILanguageRepository.LanguagesState.ERROR
import io.reactivex.disposables.Disposable
import ru.santaev.clipboardtranslator.util.RxHelper

class MainActivityViewModel : ViewModel() {

    var sharedText: String? = null
    var loading = MutableLiveData<Boolean>()
    var languagesRequestFailed = MutableLiveData<Boolean>()
    private var languagesDisposable: Disposable? = null
    private var requestLanguagesDisposable: Disposable? = null

    init {
        loading.value = true
        languagesRequestFailed.value = false
        loadLanguages()
    }

    fun reloadLanguages() {
        languagesRequestFailed.value = false
        requestLanguages()
    }

    override fun onCleared() {
        super.onCleared()
        languagesDisposable?.dispose()
        requestLanguagesDisposable?.dispose()
    }

    private fun loadLanguages() {
        languagesDisposable = UseCaseFactory
                .instance
                .getGetLanguagesUseCase()
                .execute()
                .compose(RxHelper.getFlowableTransformer())
                .subscribe { languages ->
                    loading.value = languages.isEmpty()
                    if (languages.isEmpty()) {
                        requestLanguages()
                    } else {
                        languagesDisposable?.dispose()
                    }
                }
    }

    private fun requestLanguages() {
        requestLanguagesDisposable = UseCaseFactory
                .instance
                .getRequestLanguagesUseCase()
                .execute()
                .compose(RxHelper.getTransformer())
                .subscribe(
                        { status ->
                            Log.d(TAG, "$status")
                            requestLanguagesDisposable?.dispose()
                            when (status) {
                                ERROR -> {
                                    languagesRequestFailed.value = true
                                }
                                else -> {
                                }
                            }
                        },
                        { error ->
                            languagesRequestFailed.value = true
                            requestLanguagesDisposable?.dispose()
                            Log.d(TAG, "Error while load languages", error)
                        }
                )
    }
}

private const val TAG = "MainActivityViewModel"