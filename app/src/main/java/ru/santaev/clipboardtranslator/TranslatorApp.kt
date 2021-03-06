package ru.santaev.clipboardtranslator


import android.app.Application
import android.content.Context
import com.example.santaev.domain.factory.IGatewayFactory
import ru.santaev.clipboardtranslator.di.AppComponent
import ru.santaev.clipboardtranslator.di.DaggerAppComponent
import ru.santaev.clipboardtranslator.di.module.AppModule


open class TranslatorApp : Application() {

    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this

        appComponent = buildComponent()

        IGatewayFactory.setGatewayFactory(GatewayFactory(
                appComponent.getDatabase().getLanguageDao(),
                appComponent.getDatabase().getTranslationDao(),
                appComponent.getApiService()
        ))
    }

    protected open fun buildComponent(): AppComponent {
        return DaggerAppComponent.builder()
                .appModule(AppModule(this, BuildConfig.MOCK))
                .build()
    }

    companion object {

        @JvmStatic
        lateinit var instance: TranslatorApp
            private set

        val appContext: Context
            get() = instance.applicationContext
    }
}
