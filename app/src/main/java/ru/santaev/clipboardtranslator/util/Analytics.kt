package ru.santaev.clipboardtranslator.util

import android.content.Context
import android.os.Bundle

import com.google.firebase.analytics.FirebaseAnalytics

class Analytics(context: Context) {

    private val firebaseAnalytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(context)

    fun logSelectEvent(id: String, name: String) {
        logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, id, name)
    }

    fun logViewEvent(id: String, name: String) {
        logEvent(FirebaseAnalytics.Event.VIEW_ITEM, id, name)
    }

    fun logClickEvent(id: String, name: String) {
        logEvent("click", id, name)
    }

    fun logClickEvent(id: String) {
        logEvent("click", id, id)
    }

    fun logEvent(event: String, id: String, name: String) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id)
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name)
        firebaseAnalytics.logEvent(event, bundle)
    }

}

object AnalyticsConstants {

    const val EVENT_ID_NAME_CLICK_SETTINGS = "click_settings"
    const val EVENT_ID_NAME_CLICK_SOURCE_LANG = "click_choose_source_lang"
    const val EVENT_ID_NAME_CLICK_TARGET_LANG = "click_choose_target_lang"
    const val EVENT_ID_NAME_CLICK_START_SERVICE = "click_start_service"
    const val EVENT_ID_NAME_CLICK_STOP_SERVICE = "click_start_service"
    const val EVENT_ID_NAME_CLICK_CLEAR_TEXT = "click_clear_text"
    const val EVENT_ID_NAME_CLICK_RETRY = "click_retry"
    const val EVENT_ID_NAME_CLICK_OPEN_YANDEX = "click_open_yandex"
    const val EVENT_ID_NAME_CLICK_SWIPE_DELETE = "click_swipe_delete"
    const val EVENT_ID_NAME_CLICK_DELETE_ALL_HISTORY = "click_delete_all_history"

    const val EVENT_ID_NAME_CLICK_SETTINGS_NOTIFICATION_TYPE = "click_settings_notification_type"
    const val EVENT_ID_NAME_CLICK_SETTINGS_COPY_BUTTON = "click_settings_copy_button"
    const val EVENT_ID_NAME_CLICK_SETTINGS_FEEDBACK = "click_settings_feedback"
    const val EVENT_ID_NAME_CLICK_SETTINGS_RATE = "click_settings_copy_rate"

    const val EVENT_ID_NAME_TRANSLATED = "translated"
    const val EVENT_ID_NAME_TRANSLATE_FAILED = "translate_failed"

    const val EVENT_ID_SELECT_SOURCE_LANG = "select_source_lang_"
    const val EVENT_ID_SELECT_TARGET_LANG = "select_target_lang_"
}
