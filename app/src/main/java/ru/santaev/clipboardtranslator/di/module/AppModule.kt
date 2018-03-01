package ru.santaev.clipboardtranslator.di.module

import android.arch.persistence.room.Room
import android.content.Context
import dagger.Module
import dagger.Provides
import ru.santaev.clipboardtranslator.TranslatorApp
import ru.santaev.clipboardtranslator.api.ApiService
import ru.santaev.clipboardtranslator.api.IApiService
import ru.santaev.clipboardtranslator.api.mock.MockApiService
import ru.santaev.clipboardtranslator.db.AppDatabase
import ru.santaev.clipboardtranslator.model.DataModel
import ru.santaev.clipboardtranslator.model.HistoryDataModel
import ru.santaev.clipboardtranslator.model.IDataModel
import ru.santaev.clipboardtranslator.model.IHistoryDataModel
import ru.santaev.clipboardtranslator.model.repository.ILanguageRepository
import ru.santaev.clipboardtranslator.model.repository.LanguageRepository
import ru.santaev.clipboardtranslator.model.repository.database.IAppDatabase
import javax.inject.Singleton


@Module
class AppModule(private val appContext: Context, private val mockApiService: Boolean) {

    @Provides
    fun provideContext(): Context {
        return appContext
    }

    @Provides
    @Singleton
    fun provideApiService(): IApiService {
        return if (mockApiService) MockApiService() else ApiService()
    }

    @Provides
    @Singleton
    fun provideDataModel(
            apiService: IApiService,
            appDatabase: IAppDatabase,
            languageRepository: ILanguageRepository
    ): IDataModel {
        return DataModel(apiService, appDatabase, languageRepository)
    }

    @Provides
    @Singleton
    fun provideHistoryDataModel(appDatabase: IAppDatabase): IHistoryDataModel {
        return HistoryDataModel(appDatabase.getTranslationDao())
    }

    @Provides
    @Singleton
    fun provideLanguageRepository(appDatabase: IAppDatabase, apiService: IApiService): ILanguageRepository {
        return LanguageRepository(appDatabase.getLanguageDao(), apiService)
    }

    @Provides
    @Singleton
    fun provideDatabase(): IAppDatabase {
        return Room.databaseBuilder(
                TranslatorApp.appContext,
                AppDatabase::class.java,
                AppDatabase.DB_NAME
        ).build()
    }
}

