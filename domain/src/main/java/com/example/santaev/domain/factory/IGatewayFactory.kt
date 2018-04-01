package com.example.santaev.domain.factory

import com.example.santaev.domain.api.IApiService
import com.example.santaev.domain.database.ILanguageDao
import com.example.santaev.domain.database.ITranslationDao

interface IGatewayFactory {

    fun getLanguageDao(): ILanguageDao

    fun getTranslationDao(): ITranslationDao

    fun getApiService(): IApiService

    companion object {

        private lateinit var _instance: IGatewayFactory

        val instance: IGatewayFactory
            get() = _instance

        fun setGatewayFactory(factory: IGatewayFactory) {
            _instance = factory
        }
    }
}