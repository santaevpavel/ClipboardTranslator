package ru.santaev.clipboardtranslator.di

import com.example.santaev.domain.api.IApiService
import dagger.Component
import ru.santaev.clipboardtranslator.di.module.AppModule
import ru.santaev.clipboardtranslator.model.repository.database.IAppDatabase
import javax.inject.Singleton

@Component(modules = arrayOf(AppModule::class))
@Singleton
interface AppComponent {

    /*fun inject(translateViewModel: TranslateViewModel)

    fun inject(translateViewModel: HistoryViewModel)

    fun inject(translateService: TranslateService)

    fun inject(chooseLanguageViewModel: ChooseLanguageViewModel)*/

    fun getDatabase(): IAppDatabase

    fun getApiService(): IApiService

}