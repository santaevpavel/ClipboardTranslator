package ru.santaev.clipboardtranslator.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.santaev.domain.factory.UseCaseFactory
import io.reactivex.disposables.CompositeDisposable
import ru.santaev.clipboardtranslator.util.RxHelper

class MainActivityViewModel : ViewModel() {

    var sharedText: String? = null
    var loading = MutableLiveData<Boolean>()
    private var compositeDisposable = CompositeDisposable()

    init {
        loading.value = true
        loadLanguages()
    }

    fun setLoading(loading: Boolean) {
        this.loading.value = loading
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    private fun loadLanguages() {
        val disposable = UseCaseFactory
                .instance
                .getGetLanguagesUseCase()
                .execute()
                .compose(RxHelper.getFlowableTransformer())
                .subscribe { languages ->
                    loading.value = languages.isEmpty()
                }
        compositeDisposable.add(disposable)
    }
}