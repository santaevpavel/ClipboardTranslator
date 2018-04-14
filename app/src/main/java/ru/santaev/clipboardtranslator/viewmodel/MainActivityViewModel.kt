package ru.santaev.clipboardtranslator.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.example.santaev.domain.factory.UseCaseFactory
import com.example.santaev.domain.repository.ILanguageRepository.LanguagesState.ERROR
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import ru.santaev.clipboardtranslator.util.RxHelper

class MainActivityViewModel : ViewModel() {

    var sharedText: String? = null
    var loading = MutableLiveData<Boolean>()
    var languagesRequestFailed = MutableLiveData<Boolean>()
    private var compositeDisposable = CompositeDisposable()
    private var languagesDisposable: Disposable? = null

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
        compositeDisposable.dispose()
        languagesDisposable?.dispose()
    }

    private fun loadLanguages() {
        val disposable = UseCaseFactory
                .instance
                .getGetLanguagesUseCase()
                .execute()
                .compose(RxHelper.getFlowableTransformer())
                .subscribe { languages ->
                    loading.value = languages.isEmpty()
                    if (languages.isEmpty()) {
                        requestLanguages()
                    } else {
                        compositeDisposable.dispose()
                    }
                }
        compositeDisposable.add(disposable)
    }

    private fun requestLanguages() {
        languagesDisposable = UseCaseFactory
                .instance
                .getRequestLanguagesUseCase()
                .execute()
                .compose(RxHelper.getTransformer())
                .subscribe(
                        { status ->
                            Log.d(TAG, "$status")
                            languagesDisposable?.dispose()
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
                            languagesDisposable?.dispose()
                            Log.d(TAG, "Error while load languages", error)
                        }
                )
    }
}

private const val TAG = "MainActivityViewModel"