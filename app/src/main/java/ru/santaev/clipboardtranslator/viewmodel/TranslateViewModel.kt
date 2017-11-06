package ru.santaev.clipboardtranslator.viewmodel


import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.os.Handler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import ru.santaev.clipboardtranslator.db.entity.Language
import ru.santaev.clipboardtranslator.model.IDataModel
import ru.santaev.clipboardtranslator.model.TranslateDirectionProvider
import ru.santaev.clipboardtranslator.util.settings.AppPreference
import java.util.concurrent.TimeUnit

class TranslateViewModel(private val dataModel: IDataModel) : ViewModel() {

    val translatedText = MutableLiveData<String>()
    val progress = MutableLiveData<Boolean>()
    val failed = MutableLiveData<Boolean>()
    val originLang = MutableLiveData<Language>()
    val targetLang = MutableLiveData<Language>()
    var originText: String = ""

    private var handler: Handler
    private var disposable: Disposable? = null
    private var runTranslate: Runnable? = null

    private val appPreference: AppPreference
    private val translateDirectionProvider: TranslateDirectionProvider

    init {
        translatedText.value = ""
        progress.value = false
        failed.value = false
        appPreference = AppPreference.getInstance()
        translateDirectionProvider = TranslateDirectionProvider()

        originLang.value = appPreference.originLang
        targetLang.value = appPreference.targetLang

        handler = Handler()
    }

    fun onOriginTextChanged(text: String) {
        originText = text
        translate()
    }

    fun onOriginLangSelected(lang: Language): Boolean {
        val oldLang = originLang.value
        originLang.value = lang
        appPreference.originLang = lang

        val support = translateDirectionProvider.isSupportTranslate(lang, targetLang.value)

        if (lang !== oldLang && support) {
            translate()
        }
        return support
    }

    fun onTargetLangSelected(lang: Language): Boolean {
        val oldLang = targetLang.value
        targetLang.value = lang
        appPreference.targetLang = lang

        val support = translateDirectionProvider.isSupportTranslate(originLang.value, lang)

        if (lang !== oldLang && support) {
            translate()
        }
        return support
    }

    fun onClickRetry() {
        translate()
    }

    private fun translate() {
        if (originText.trim { it <= ' ' }.isEmpty()) {
            return
        }
        disposable?.dispose()

        if (runTranslate != null) {
            handler.removeCallbacks(runTranslate)
        }

        runTranslate = Runnable {
            progress.value = true
            failed.value = false

            disposable = dataModel.translate(originLang.value, targetLang.value, originText)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ translateResponse ->
                        translatedText.value = translateResponse.targetText
                        progress.value = false
                    }) { throwable ->
                        translatedText.value = throwable.message
                        progress.value = false
                        failed.value = true
                    }
        }

        handler.postDelayed(runTranslate, TRANSLATE_DELAY_MILLIS)
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
        runTranslate?.let {
            handler.removeCallbacks(runTranslate)
        }
    }

    companion object {

        private val TRANSLATE_DELAY_MILLIS = TimeUnit.MILLISECONDS.toMillis(500)
    }
}
