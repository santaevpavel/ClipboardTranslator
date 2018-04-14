package ru.santaev.clipboardtranslator.viewmodel


import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.santaev.domain.dto.LanguageDto
import com.example.santaev.domain.factory.UseCaseFactory
import com.example.santaev.domain.usecase.TranslateUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ru.santaev.clipboardtranslator.util.ILoggable
import ru.santaev.clipboardtranslator.util.RxHelper
import ru.santaev.clipboardtranslator.util.settings.AppPreference
import java.util.concurrent.TimeUnit

class TranslateViewModel : ViewModel(), ILoggable {

    val translatedText = MutableLiveData<String>()
    val progress = MutableLiveData<Boolean>()
    val failed = MutableLiveData<Boolean>()
    val sourceLanguage = MutableLiveData<LanguageDto?>()
    val targetLanguage = MutableLiveData<LanguageDto?>()
    var originText: String = ""
    private var translateDisposable: Disposable? = null
    private var languagesDisposable: Disposable? = null
    private val appPreference: AppPreference

    init {
        translatedText.value = ""
        progress.value = false
        failed.value = false
        appPreference = AppPreference.getInstance()

        val storedSourceLanguage = appPreference.originLang
        val storedTargetLanguage = appPreference.targetLang

        if (storedSourceLanguage == null || storedTargetLanguage == null) {
            loadLanguages()
        } else {
            sourceLanguage.value = storedSourceLanguage
            targetLanguage.value = storedTargetLanguage
        }
    }

    fun onOriginTextChanged(text: String) {
        originText = text
        translate()
    }

    fun onOriginLangSelected(lang: LanguageDto): Boolean {
        val oldLang = sourceLanguage.value
        sourceLanguage.value = lang
        appPreference.originLang = lang

        if (lang != oldLang) {
            translate()
        }
        return true
    }

    fun onTargetLangSelected(lang: LanguageDto): Boolean {
        val oldLang = targetLanguage.value
        targetLanguage.value = lang
        appPreference.targetLang = lang

        if (lang != oldLang) {
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

    override fun onCleared() {
        super.onCleared()
        translateDisposable?.dispose()
        languagesDisposable?.dispose()
    }

    private fun translate() {
        if (originText.trim { it <= ' ' }.isEmpty()) return

        val sourceLang = sourceLanguage.value ?: return
        val targetLang = targetLanguage.value ?: return

        progress.value = true
        failed.value = false

        translateDisposable?.dispose()

        translateDisposable = UseCaseFactory
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

    private fun loadLanguages() {
        languagesDisposable = UseCaseFactory
                .instance
                .getGetLanguagesUseCase()
                .execute()
                .filter { it.isNotEmpty() }
                .firstOrError()
                .compose(RxHelper.getSingleTransformer())
                .subscribe(
                        { languages -> setSourceAndTargetLanguages(languages) },
                        { error -> log("Unable to load languages", error) }
                )
    }

    private fun setSourceAndTargetLanguages(languages: List<LanguageDto>) {
        if (languages.isNotEmpty()) {
            val sourceLanguageDto = languages.firstOrNull { it.code == "en" } ?: languages.first()
            val targetLanguageDto = languages.firstOrNull { it.code == "ru" } ?: languages.first()
            sourceLanguage.value = sourceLanguageDto
            targetLanguage.value = targetLanguageDto
            appPreference.originLang = sourceLanguageDto
            appPreference.targetLang = targetLanguageDto
        }
    }

    companion object {

        private val TRANSLATE_DELAY_MILLIS = TimeUnit.MILLISECONDS.toMillis(500)
    }
}
