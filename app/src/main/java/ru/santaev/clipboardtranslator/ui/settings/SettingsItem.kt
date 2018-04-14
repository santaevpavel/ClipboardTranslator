package ru.santaev.clipboardtranslator.ui.settings

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.databinding.ObservableField

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
