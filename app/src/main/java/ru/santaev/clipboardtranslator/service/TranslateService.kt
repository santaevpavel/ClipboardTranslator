package ru.santaev.clipboardtranslator.service

import android.app.Application
import android.app.Notification
import android.app.NotificationManager
import android.app.Service
import android.content.*
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.util.Pair
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.santaev.clipboardtranslator.R
import ru.santaev.clipboardtranslator.TranslatorApp
import ru.santaev.clipboardtranslator.model.IDataModel
import ru.santaev.clipboardtranslator.service.util.ClipboardFilter
import ru.santaev.clipboardtranslator.service.util.IClipboardFilter
import ru.santaev.clipboardtranslator.service.util.ITranslationSettingsProvider
import ru.santaev.clipboardtranslator.util.NotificationHelper
import ru.santaev.clipboardtranslator.util.settings.AppPreference
import ru.santaev.clipboardtranslator.util.settings.ISettings
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class TranslateService : Service(), ClipboardManager.OnPrimaryClipChangedListener,
        SharedPreferences.OnSharedPreferenceChangeListener {

    @Inject
    internal lateinit var dataModel: IDataModel

    private lateinit var clipboardManager: ClipboardManager
    private lateinit var filter: IClipboardFilter
    private lateinit var translationSettingsProvider: ITranslationSettingsProvider
    private lateinit var settings: ISettings
    private lateinit var handler: Handler

    private val notifications: MutableList<Int> = mutableListOf()
    private var currentNotificationId = Random().nextInt()

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (ACTION_COPY == intent.action) {
                Log.d(TAG, "onReceiveBroadcast")
                intent.extras?.let {
                    val text = it.getString(NotificationHelper.KEY_TEXT, null)
                    text?.let {
                        val clipData = ClipData(CLIPBOARD_DATA_LABEL,
                                arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN), ClipData.Item(text))
                        clipboardManager.primaryClip = clipData
                        Toast.makeText(context.applicationContext, R.string.toast_text_copied, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()
        TranslatorApp.instance.appComponent.inject(this)

        Log.d(TAG, "onCreate")

        handler = Handler()
        filter = ClipboardFilter()
        translationSettingsProvider = AppPreference.getInstance()
        settings = AppPreference.getInstance()
        AppPreference.getAppSharedPreference().registerOnSharedPreferenceChangeListener(this)
        registerReceiver(broadcastReceiver, IntentFilter(ACTION_COPY))

        initClipboardListener()
        showAppNotification()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
        clearRegisteredNotification()
        hideAppNotification()
        releaseClipboardListener()
        AppPreference.getAppSharedPreference().unregisterOnSharedPreferenceChangeListener(this)
        unregisterReceiver(broadcastReceiver)
    }

    private fun initClipboardListener() {
        clipboardManager = getSystemService(Application.CLIPBOARD_SERVICE) as ClipboardManager
        clipboardManager.addPrimaryClipChangedListener(this)
    }

    private fun releaseClipboardListener() {
        clipboardManager.removePrimaryClipChangedListener(this)
    }

    private fun showAppNotification() {
        val originLang = translationSettingsProvider.originLang.code
        val targetLang = translationSettingsProvider.targetLang.code

        val langText = String.format(getString(R.string.translate_notification_lang_text),
                originLang.toUpperCase(), targetLang.toUpperCase())

        val notification = NotificationHelper.buildAppNotification(applicationContext, langText)
        val notificationManager = getSystemService(Application.NOTIFICATION_SERVICE) as? NotificationManager
        notificationManager?.notify(SERVICE_NOTIFICATION_ID, notification)
    }

    private fun hideAppNotification() {
        val notificationManager = getSystemService(Application.NOTIFICATION_SERVICE) as? NotificationManager
        notificationManager?.cancel(SERVICE_NOTIFICATION_ID)
    }

    override fun onPrimaryClipChanged() {
        Log.d(TAG, "onPrimaryClipChanged")
        val clipData = clipboardManager.primaryClip
        if (clipData != null && clipData.itemCount > 0) {
            val item = clipData.getItemAt(0)
            val text = item.text
            if (text != null && filter.apply(item, clipData.description)) {
                Log.d(TAG, "onPrimaryClipChanged text = " + text)
                translate(text.toString())
            }
        }
    }

    private fun translate(text: String) {
        val originLang = translationSettingsProvider.originLang
        val targetLang = translationSettingsProvider.targetLang
        val id = currentNotificationId++
        val langText = String.format("%s-%s", originLang.code.toUpperCase(),
                targetLang.code.toUpperCase())

        showNotification(String.format(getString(R.string.translate_notification_translating), text), langText, id, false)

        dataModel.translate(originLang, targetLang, text)
                .map { translateResponse ->
                    Pair<IDataModel.TranslateResponse, String>(translateResponse, translateResponse.targetText)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ translateResponse ->
                    registerNotification(id)
                    showNotification(translateResponse.second, langText, id, true)
                }
                ) { throwable ->
                    Log.d(TAG, throwable.message)
                    registerNotification(id)
                    showNotification(getString(R.string.translate_notification_failed), langText, id, false)
                }
    }

    private fun registerNotification(id: Int) {
        val delay = when (settings.notificationClearDelay) {
            ISettings.NOTIFICATION_DELAY_NONE -> -1
            ISettings.NOTIFICATION_DELAY_FIVE -> 5
            ISettings.NOTIFICATION_DELAY_TEN -> 10
            ISettings.NOTIFICATION_DELAY_TWENTY -> 20
            else -> -1
        }
        if (delay != -1) {
            notifications.add(id)
            handler.postDelayed({
                if (notifications.contains(id)) {
                    notifications.remove(id)
                    hideNotification(id)
                }
            }, TimeUnit.SECONDS.toMillis(delay.toLong()))
        }
    }

    private fun clearRegisteredNotification() {
        notifications.clear()
        handler.removeCallbacksAndMessages(null)
    }

    private fun showNotification(text: String, langText: String, id: Int, enableCopyButton: Boolean) {
        if (ISettings.NOTIFICATION_TYPE_PUSH == settings.notificationType) {
            val notification: Notification = if (enableCopyButton && settings.isNotificationButtonEnabled) {
                NotificationHelper.buildTranslateNotification(applicationContext,
                        text, langText, 0, ACTION_COPY)
            } else {
                NotificationHelper.buildTranslateNotification(applicationContext,
                        text, langText)
            }

            val notificationManager = getSystemService(Application.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(id, notification)
        } else {
            Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
        }
    }

    private fun hideNotification(id: Int) {
        val notificationManager = getSystemService(Application.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(id)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (AppPreference.KEY_ORIGIN_LANG_CODE == key || AppPreference.KEY_TARGET_LANG_CODE == key) {
            showAppNotification()
        }
    }

    companion object {

        @JvmField
        val CLIPBOARD_DATA_LABEL = "LabelNotTranslate"

        private val TAG = "TranslateService"
        private val SERVICE_NOTIFICATION_ID = -1
        private val ACTION_COPY = "ru.santaev.clipboardtranslator.service.ActionCopy"
    }
}
