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
    fun provideDataModel(apiService: IApiService, appDatabase: AppDatabase): IDataModel {
        return DataModel(apiService, appDatabase)
    }

    @Provides
    @Singleton
    fun provideHistoryDataModel(appDatabase: AppDatabase): IHistoryDataModel {
        return HistoryDataModel(appDatabase.translationDao)
    }

    @Provides
    @Singleton
    fun provideDatabase(): AppDatabase {
        return Room.databaseBuilder(
                TranslatorApp.appContext,
                AppDatabase::class.java,
                AppDatabase.DB_NAME
        ).build()
    }
}

