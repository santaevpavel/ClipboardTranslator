package ru.santaev.clipboardtranslator


import android.arch.persistence.room.Room
import ru.santaev.clipboardtranslator.db.AppDatabase
import ru.santaev.clipboardtranslator.model.DataModel
import ru.santaev.clipboardtranslator.model.IDataModel
import ru.santaev.clipboardtranslator.test_models.MockApiService


class TestTranslatorApp : TranslatorApp() {

    override fun buildDataModel(): IDataModel {
        return DataModel(MockApiService())
    }

    override fun buildDatabase(): AppDatabase {
        return Room.inMemoryDatabaseBuilder(getAppContext(), AppDatabase::class.java)
                .allowMainThreadQueries()
                .build()
    }
}
