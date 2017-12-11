package ru.santaev.clipboardtranslator.service;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import java.util.Random;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.santaev.clipboardtranslator.R;
import ru.santaev.clipboardtranslator.TranslatorApp;
import ru.santaev.clipboardtranslator.db.entity.Language;
import ru.santaev.clipboardtranslator.model.IDataModel;
import ru.santaev.clipboardtranslator.service.util.ClipboardFilter;
import ru.santaev.clipboardtranslator.service.util.IClipboardFilter;
import ru.santaev.clipboardtranslator.service.util.ITranslationSettingsProvider;
import ru.santaev.clipboardtranslator.util.NotificationHelper;
import ru.santaev.clipboardtranslator.util.settings.AppPreference;
import ru.santaev.clipboardtranslator.util.settings.ISettings;

public class TranslateService extends Service implements ClipboardManager.OnPrimaryClipChangedListener, SharedPreferences.OnSharedPreferenceChangeListener {
    public static final String CLIPBOARD_DATA_LABEL = "LabelNotTranslate";

    private static final String TAG = "TranslateService";
    private static final int SERVICE_NOTIFICATION_ID = -1;
    private static final String ACTION_COPY = "ru.santaev.clipboardtranslator.service.ActionCopy";

    @Inject
    IDataModel dataModel;

    private ClipboardManager clipboardManager;
    private IClipboardFilter filter;
    private ITranslationSettingsProvider translationSettingsProvider;
    private ISettings settings;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ACTION_COPY.equals(intent.getAction())) {
                Log.d(TAG, "onReceiveBroadcast");
                Bundle extras = intent.getExtras();
                if (extras != null) {
                    String text = extras.getString(NotificationHelper.KEY_TEXT, null);
                    if (text != null) {
                        ClipData clipData = new ClipData(CLIPBOARD_DATA_LABEL,
                                new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, new ClipData.Item(text));
                        clipboardManager.setPrimaryClip(clipData);
                        Toast.makeText(context.getApplicationContext(), R.string.toast_text_copied, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    };

    public TranslateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        TranslatorApp.getInstance().getAppComponent().inject(this);

        Log.d(TAG, "onCreate");

        filter = new ClipboardFilter();
        translationSettingsProvider =  AppPreference.getInstance();
        settings = AppPreference.getInstance();
        AppPreference.getAppSharedPreference().registerOnSharedPreferenceChangeListener(this);
        registerReceiver(broadcastReceiver, new IntentFilter(ACTION_COPY));

        initClipboardListener();
        showAppNotification();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        hideAppNotification();
        releaseClipboardListener();
        AppPreference.getAppSharedPreference().unregisterOnSharedPreferenceChangeListener(this);
        unregisterReceiver(broadcastReceiver);
        dataModel = null;
    }

    private void initClipboardListener(){
        clipboardManager = (ClipboardManager) getSystemService(Application.CLIPBOARD_SERVICE);
        if (clipboardManager != null) {
            clipboardManager.addPrimaryClipChangedListener(this);
        }
    }

    private void releaseClipboardListener(){
        clipboardManager.removePrimaryClipChangedListener(this);
        clipboardManager = null;
    }

    private void showAppNotification(){
        String originLang = translationSettingsProvider.getOriginLang().getCode();
        String targetLang = translationSettingsProvider.getTargetLang().getCode();

        String langText = String.format(getString(R.string.translate_notification_lang_text),
                originLang.toUpperCase(), targetLang.toUpperCase());

        Notification notification = NotificationHelper.buildAppNotification(getApplicationContext(), langText);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Application.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(SERVICE_NOTIFICATION_ID, notification);
        }
    }

    private void hideAppNotification(){
        NotificationManager notificationManager = (NotificationManager) getSystemService(Application.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.cancel(SERVICE_NOTIFICATION_ID);
        }
    }

    @Override
    public void onPrimaryClipChanged() {
        Log.d(TAG, "onPrimaryClipChanged");
        if (clipboardManager == null){
            return;
        }
        ClipData clipData = clipboardManager.getPrimaryClip();
        if (clipData != null && clipData.getItemCount() > 0){
            ClipData.Item item = clipData.getItemAt(0);
            CharSequence text = item.getText();
            if (text != null && filter != null && filter.filter(item, clipData.getDescription())){
                Log.d(TAG, "onPrimaryClipChanged text = " + text);
                translate(text.toString());
            }
        }
    }

    private void translate(String text){
        Language originLang = translationSettingsProvider.getOriginLang();
        Language targetLang = translationSettingsProvider.getTargetLang();

        int id = new Random().nextInt();
        String langText = String.format("%s-%s", originLang.getCode().toUpperCase(),
                targetLang.getCode().toUpperCase());
        showNotification(String.format(getString(R.string.translate_notification_translating), text),
                langText, id, false);

        dataModel.translate(originLang, targetLang, text)
                .map(translateResponse -> new Pair<>(translateResponse, translateResponse.getTargetText()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(translateResponse -> showNotification(translateResponse.second, langText, id, true),
                        throwable -> showNotification(getString(R.string.translate_notification_failed),
                                langText, id, false));
    }

    private void showNotification(String text, String langText, int id, boolean enableCopyButton) {
        if (ISettings.NOTIFICATION_TYPE_PUSH == settings.getNotificationType()) {
            Notification notification;
            if (enableCopyButton && settings.isNotificationButtonEnabled()) {
                notification = NotificationHelper.buildTranslateNotification(getApplicationContext(),
                        text, langText, 0, ACTION_COPY);
            } else {
                notification = NotificationHelper.buildTranslateNotification(getApplicationContext(),
                        text, langText);
            }

            NotificationManager notificationManager = (NotificationManager) getSystemService(Application.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.notify(id, notification);
            }
        } else {
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (AppPreference.KEY_ORIGIN_LANG_CODE.equals(key)
                || AppPreference.KEY_TARGET_LANG_CODE.equals(key)) {
            showAppNotification();
        }
    }
}
