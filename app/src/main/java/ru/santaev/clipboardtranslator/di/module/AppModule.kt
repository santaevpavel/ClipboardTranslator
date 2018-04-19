package ru.santaev.clipboardtranslator.di.module

import android.arch.persistence.room.Room
import android.content.Context
import com.example.santaev.domain.api.IApiService
import dagger.Module
import dagger.Provides
import ru.santaev.clipboardtranslator.TranslatorApp
import ru.santaev.clipboardtranslator.api.ApiServiceFactory
import ru.santaev.clipboardtranslator.api.mock.MockApiService
import ru.santaev.clipboardtranslator.db.AppDatabase
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
        return if (mockApiService) MockApiService() else ApiServiceFactory.instance.getAbbyyApiService()
    }

    /*
    @Provides
    @Singleton
    fun provideDataModel(
            apiService: IApiService,
            translationRepository: ITranslationRepository,
            languageRepository: ILanguageRepository
    ): IDataModel {
        return DataModel(apiService, translationRepository, languageRepository)
    }

    @Provides
    @Singleton
    fun provideHistoryDataModel(translationRepository: ITranslationRepository): IHistoryDataModel {
        return HistoryDataModel(translationRepository)
    }

    @Provides
    @Singleton
    fun provideLanguageRepository(appDatabase: IAppDatabase, apiService: IApiService): ILanguageRepository {
        return LanguageRepository(appDatabase.getLanguageDao(), apiService)
    }

    @Provides
    @Singleton
    fun provideTranslationRepository(appDatabase: IAppDatabase): ITranslationRepository {
        return TranslationRepository(appDatabase.getTranslationDao())
    }
*/
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

