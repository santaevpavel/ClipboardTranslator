package ru.santaev.clipboardtranslator.ui.settings

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.ObservableField

open class SettingsItem(
        title: String,
        subtitle: String
) : BaseObservable() {

    @Bindable
    val title = ObservableField<String>()

    @Bindable
    val subtitle = ObservableField<String>()

    init {
        this.title.set(title)
        this.subtitle.set(subtitle)
    }
}
