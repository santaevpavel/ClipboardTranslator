package ru.santaev.clipboardtranslator.util

import android.util.Log

interface ILoggable {

    fun tag(): String = javaClass.simpleName

    fun log(message: String, throwable: Throwable? = null) {
        Log.d(tag(), message, throwable)
    }
}