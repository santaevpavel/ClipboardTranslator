package ru.santaev.clipboardtranslator


import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context

import ru.santaev.clipboardtranslator.api.ApiService
import ru.santaev.clipboardtranslator.api.mock.MockApiService
import ru.santaev.clipboardtranslator.db.AppDatabase
import ru.santaev.clipboardtranslator.model.DataModel
import ru.santaev.clipboardtranslator.model.HistoryDataModel
import ru.santaev.clipboardtranslator.model.IDataModel
import ru.santaev.clipboardtranslator.model.IHistoryDataModel

open class TranslatorApp : Application() {

    lateinit var dataModel: IDataModel
        private set
    lateinit var historyDataModel: IHistoryDataModel
        private set
    lateinit var database: AppDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this

        database = buildDatabase()
        dataModel = buildDataModel()
        historyDataModel = buildHistoryDataModel()
    }

    protected open fun buildDataModel(): IDataModel {
        val apiService = if (BuildConfig.MOCK) MockApiService() else ApiService()
        return DataModel(apiService)
    }

    protected fun buildHistoryDataModel(): IHistoryDataModel {
        return HistoryDataModel()
    }

    protected open fun buildDatabase(): AppDatabase {
        return Room.databaseBuilder(TranslatorApp.appContext, AppDatabase::class.java, AppDatabase.DB_NAME)
                .build()
    }

    companion object {

        @JvmStatic lateinit var instance: TranslatorApp
            private set

        val appContext: Context
            get() = instance.applicationContext
    }
}
