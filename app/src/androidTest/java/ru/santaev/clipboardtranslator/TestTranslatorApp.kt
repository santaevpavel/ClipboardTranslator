package ru.santaev.clipboardtranslator


import android.arch.persistence.room.Room
import ru.santaev.clipboardtranslator.api.mock.MockApiService
import ru.santaev.clipboardtranslator.db.AppDatabase
import ru.santaev.clipboardtranslator.model.DataModel
import ru.santaev.clipboardtranslator.model.IDataModel


class TestTranslatorApp : TranslatorApp() {

    override fun buildDataModel(): IDataModel {
        return DataModel(MockApiService())
    }

    override fun buildDatabase(): AppDatabase {
        return Room.inMemoryDatabaseBuilder(appContext, AppDatabase::class.java)
                .allowMainThreadQueries()
                .build()
    }
}
