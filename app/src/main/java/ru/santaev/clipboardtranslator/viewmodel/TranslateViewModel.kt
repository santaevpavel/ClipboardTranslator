package ru.santaev.clipboardtranslator.viewmodel


import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.santaev.domain.dto.LanguageDto
import com.example.santaev.domain.factory.UseCaseFactory
import com.example.santaev.domain.usecase.TranslateUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.santaev.clipboardtranslator.util.settings.AppPreference
import java.util.concurrent.TimeUnit

class TranslateViewModel : ViewModel() {

    val translatedText = MutableLiveData<String>()
    val progress = MutableLiveData<Boolean>()
    val failed = MutableLiveData<Boolean>()
    val sourceLanguage = MutableLiveData<LanguageDto>()
    val targetLanguage = MutableLiveData<LanguageDto>()
    var originText: String = ""
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val appPreference: AppPreference

    init {
        translatedText.value = ""
        progress.value = false
        failed.value = false
        appPreference = AppPreference.getInstance()

        sourceLanguage.value = appPreference.originLang
        targetLanguage.value = appPreference.targetLang
    }

    fun onOriginTextChanged(text: String) {
        originText = text
        translate()
    }

    fun onOriginLangSelected(lang: LanguageDto): Boolean {
        val oldLang = sourceLanguage.value
        sourceLanguage.value = lang
        appPreference.originLang = lang

        if (lang !== oldLang) {
            translate()
        }
        return true
    }

    fun onTargetLangSelected(lang: LanguageDto): Boolean {
        val oldLang = targetLanguage.value
        targetLanguage.value = lang
        appPreference.targetLang = lang

        if (lang !== oldLang) {
            translate()
        }
        return true
    }

    fun onClickRetry() {
        translate()
    }

    fun onClickSwipeLanguages() {
        val lang = targetLanguage.value
        targetLanguage.value = sourceLanguage.value
        sourceLanguage.value = lang

        appPreference.originLang = sourceLanguage.value
        appPreference.targetLang = targetLanguage.value

        translate()
    }

    private fun translate() {
        if (originText.trim { it <= ' ' }.isEmpty()) return

        val sourceLang = sourceLanguage.value ?: return
        val targetLang = targetLanguage.value ?: return

        progress.value = true
        failed.value = false

        compositeDisposable.dispose()

        val disposable = UseCaseFactory
                .instance
                .getTranslateFactory(
                        sourceLang,
                        targetLang,
                        originText
                )
                .execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { response -> onTextTranslated(response) },
                        { onTranslateFailed() }
                )
        compositeDisposable.addAll(disposable)
    }

    private fun onTextTranslated(response: TranslateUseCase.Response) {
        when (response) {
            is TranslateUseCase.Response.Success -> {
                translatedText.value = response.targetText
                progress.value = false
            }
            is TranslateUseCase.Response.Error -> onTranslateFailed()
        }
    }

    private fun onTranslateFailed() {
        translatedText.value = "Error"
        progress.value = false
        failed.value = true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    companion object {

        private val TRANSLATE_DELAY_MILLIS = TimeUnit.MILLISECONDS.toMillis(500)
    }
}
