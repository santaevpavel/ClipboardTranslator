package ru.santaev.clipboardtranslator.api.abbyy

interface IAbbyyApiTokenKeeper {

    fun saveToken(token: String)

    fun getToken(): String?
}