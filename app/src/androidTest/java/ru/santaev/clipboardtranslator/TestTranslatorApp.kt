package ru.santaev.clipboardtranslator

import ru.santaev.clipboardtranslator.di.AppComponent
import ru.santaev.clipboardtranslator.di.DaggerAppComponent
import ru.santaev.clipboardtranslator.di.module.AppModule


class TestTranslatorApp : TranslatorApp() {

    override fun buildComponent(): AppComponent {
        return DaggerAppComponent.builder()
                .appModule(AppModule(this, true))
                .build()
    }
}
