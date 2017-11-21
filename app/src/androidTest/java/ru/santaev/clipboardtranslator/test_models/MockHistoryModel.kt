package ru.santaev.clipboardtranslator.test_models

import io.reactivex.Flowable
import ru.santaev.clipboardtranslator.db.entity.Translation
import ru.santaev.clipboardtranslator.model.IHistoryDataModel


class MockHistoryModel : IHistoryDataModel {

    override val translationHistory: Flowable<List<Translation>>
        get() = Flowable.just(store)

    override fun removeTranslation(translation: Translation) {
        store.removeIf({ t -> t.id == translation.id })
    }

    override fun deleteAll() {
        store.clear()
    }

    companion object {

        val store: MutableList<Translation> = arrayListOf<Translation>()
    }

}