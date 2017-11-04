package ru.santaev.clipboardtranslator.util;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

public class Analytics {

    public static final String EVENT_ID_NAME_CLICK_SETTINGS = "click_settings";
    public static final String EVENT_ID_NAME_CLICK_SOURCE_LANG = "click_choose_source_lang";
    public static final String EVENT_ID_NAME_CLICK_TARGET_LANG = "click_choose_target_lang";
    public static final String EVENT_ID_NAME_CLICK_START_SERVICE = "click_start_service";
    public static final String EVENT_ID_NAME_CLICK_STOP_SERVICE = "click_start_service";
    public static final String EVENT_ID_NAME_CLICK_CLEAR_TEXT = "click_clear_text";
    public static final String EVENT_ID_NAME_CLICK_RETRY = "click_retry";
    public static final String EVENT_ID_NAME_CLICK_OPEN_YANDEX = "click_open_yandex";
    public static final String EVENT_ID_NAME_CLICK_SWIPE_DELETE = "click_swipe_delete";
    public static final String EVENT_ID_NAME_CLICK_DELETE_ALL_HISTORY = "click_delete_all_history";

    public static final String EVENT_ID_NAME_CLICK_SETTINGS_NOTIFICATION_TYPE = "click_settings_notification_type";
    public static final String EVENT_ID_NAME_CLICK_SETTINGS_COPY_BUTTON = "click_settings_copy_button";
    public static final String EVENT_ID_NAME_CLICK_SETTINGS_FEEDBACK = "click_settings_feedback";
    public static final String EVENT_ID_NAME_CLICK_SETTINGS_RATE = "click_settings_copy_rate";

    public static final String EVENT_ID_NAME_TRANSLATED = "translated";
    public static final String EVENT_ID_NAME_TRANSLATE_FAILED = "translate_failed";

    public static final String EVENT_ID_SELECT_SOURCE_LANG = "select_source_lang_";
    public static final String EVENT_ID_SELECT_TARGET_LANG = "select_target_lang_";

    private final FirebaseAnalytics firebaseAnalytics;

    public Analytics(Context context) {
        firebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    public void logSelectEvent(String id, String name) {
        logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, id, name);
    }

    public void logViewEvent(String id, String name) {
        logEvent(FirebaseAnalytics.Event.VIEW_ITEM, id, name);
    }

    public void logClickEvent(String id, String name) {
        logEvent("click", id, name);
    }

    public void logClickEvent(String id) {
        logEvent("click", id, id);
    }

    public void logEvent(String event, String id, String name) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
        firebaseAnalytics.logEvent(event, bundle);
    }
}
