package ru.santaev.clipboardtranslator.model

import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import ru.santaev.clipboardtranslator.db.dao.TranslationDao
import ru.santaev.clipboardtranslator.db.entity.Translation
import ru.santaev.clipboardtranslator.util.RxHelper

class HistoryDataModel(private val translationDao: TranslationDao) : IHistoryDataModel {

    override val translationHistory: Flowable<List<Translation>>
        get() = translationDao.translations.subscribeOn(Schedulers.io())

    override fun removeTranslation(translation: Translation) {
        Observable.fromCallable { translationDao.delete(translation) }
                .compose(RxHelper.getAsyncTransformer())
                .subscribe()
    }

    override fun deleteAll() {
        Observable.fromCallable { translationDao.deleteAll() }
                .compose(RxHelper.getAsyncTransformer())
                .subscribe()
    }

}