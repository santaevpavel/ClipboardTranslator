package ru.santaev.clipboardtranslator.domain

import com.example.santaev.domain.api.IApiService
import com.example.santaev.domain.database.ILanguageDao
import com.example.santaev.domain.database.ITranslationDao
import com.example.santaev.domain.factory.IGatewayFactory


class GatewayFactory : IGatewayFactory {

    override fun getLanguageDao(): ILanguageDao {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTranslationDao(): ITranslationDao {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getApiService(): IApiService {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}