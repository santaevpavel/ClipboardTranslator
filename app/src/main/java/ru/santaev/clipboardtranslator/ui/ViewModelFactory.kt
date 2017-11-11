package ru.santaev.clipboardtranslator.ui

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

import ru.santaev.clipboardtranslator.model.IDataModel
import ru.santaev.clipboardtranslator.viewmodel.ChooseLanguageViewModel
import ru.santaev.clipboardtranslator.viewmodel.TranslateViewModel

internal class ViewModelFactory(private val dataModel: IDataModel) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass == TranslateViewModel::class.java) {
            TranslateViewModel(dataModel) as T
        } else if (modelClass == ChooseLanguageViewModel::class.java) {
            ChooseLanguageViewModel(dataModel) as T
        } else {
            throw IllegalArgumentException("Unknown class")
        }
    }

}
