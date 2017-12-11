package ru.santaev.clipboardtranslator.viewmodel


import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.santaev.clipboardtranslator.TranslatorApp
import ru.santaev.clipboardtranslator.db.entity.Language
import ru.santaev.clipboardtranslator.model.IDataModel
import java.util.*
import javax.inject.Inject

class ChooseLanguageViewModel() : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    val languages = MutableLiveData<List<Language>>()
    @Inject lateinit var dataModel: IDataModel

    init {
        TranslatorApp.instance.appComponent.inject(this)

        this.languages.value = ArrayList()

        compositeDisposable.add(dataModel.languages
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ this.onLanguagesChanged(it) }))
    }

    private fun onLanguagesChanged(languages: List<Language>) {
        this.languages.postValue(languages)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}
