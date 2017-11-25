package ru.santaev.clipboardtranslator.util

import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


object RxHelper {

    fun <T> getAsyncTransformer(): ObservableTransformer<T, T> {
        return ObservableTransformer { upstream ->
            upstream.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }

}
