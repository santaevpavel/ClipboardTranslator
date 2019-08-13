package ru.santaev.clipboardtranslator.viewmodel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.santaev.domain.dto.LanguageDto
import com.example.santaev.domain.factory.UseCaseFactory
import io.reactivex.disposables.CompositeDisposable
import ru.santaev.clipboardtranslator.util.RxHelper
import java.util.*

class ChooseLanguageViewModel() : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    val languages = MutableLiveData<List<LanguageDto>>()

    init {
        this.languages.value = ArrayList()

        val disposable = UseCaseFactory
                .instance
                .getGetLanguagesUseCase()
                .execute()
                .compose(RxHelper.getFlowableTransformer())
                .subscribe(
                        { onLanguagesChanged(it) },
                        { onLanguagesChanged(listOf()) }
                )
        compositeDisposable.add(disposable)
    }

    private fun onLanguagesChanged(languages: List<LanguageDto>) {
        this.languages.postValue(languages)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}
