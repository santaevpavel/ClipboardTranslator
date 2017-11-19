package ru.santaev.clipboardtranslator;


import android.app.Application;
import android.content.Context;

import ru.santaev.clipboardtranslator.api.ApiService;
import ru.santaev.clipboardtranslator.db.AppDatabase;
import ru.santaev.clipboardtranslator.model.DataModel;
import ru.santaev.clipboardtranslator.model.HistoryDataModel;
import ru.santaev.clipboardtranslator.model.IDataModel;
import ru.santaev.clipboardtranslator.model.IHistoryDataModel;

public class TranslatorApp extends Application{

    private static TranslatorApp instance;

    public static TranslatorApp getInstance(){
        return instance;
    }

    public static Context getAppContext(){
        return getInstance().getApplicationContext();
    }

    private IDataModel dataModel;
    private IHistoryDataModel historyDataModel;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        AppDatabase.init(getAppContext());

        dataModel = buildDataModel();
        historyDataModel = buildHistoryDataModel();
    }

    public IDataModel getDataModel() {
        return dataModel;
    }

    public IHistoryDataModel getHistoryDataModel() {
        return historyDataModel;
    }

    protected IDataModel buildDataModel(){
        return new DataModel(new ApiService());
    }

    protected IHistoryDataModel buildHistoryDataModel() {
        return new HistoryDataModel();
    }

}
