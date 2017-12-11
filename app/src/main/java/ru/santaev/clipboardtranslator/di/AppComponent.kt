package ru.santaev.clipboardtranslator.di

import dagger.Component
import ru.santaev.clipboardtranslator.di.module.AppModule
import ru.santaev.clipboardtranslator.service.TranslateService
import ru.santaev.clipboardtranslator.viewmodel.ChooseLanguageViewModel
import ru.santaev.clipboardtranslator.viewmodel.HistoryViewModel
import ru.santaev.clipboardtranslator.viewmodel.TranslateViewModel
import javax.inject.Singleton

@Component(modules = arrayOf(AppModule::class))
@Singleton
interface AppComponent {

    fun inject(translateViewModel: TranslateViewModel)

    fun inject(translateViewModel: HistoryViewModel)

    fun inject(translateService: TranslateService)

    fun inject(chooseLanguageViewModel: ChooseLanguageViewModel)

}