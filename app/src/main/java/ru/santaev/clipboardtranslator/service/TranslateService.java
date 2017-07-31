package ru.santaev.clipboardtranslator.service;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import ru.santaev.clipboardtranslator.api.ApiService;
import ru.santaev.clipboardtranslator.api.TranslateRequest;
import ru.santaev.clipboardtranslator.api.TranslateResponse;
import ru.santaev.clipboardtranslator.db.AppDatabase;
import ru.santaev.clipboardtranslator.db.entity.Translation;
import ru.santaev.clipboardtranslator.model.Language;
import ru.santaev.clipboardtranslator.service.uitl.ClipboardFilter;
import ru.santaev.clipboardtranslator.service.uitl.IClipboardFilter;
import ru.santaev.clipboardtranslator.service.uitl.ITranslationSettingsProvider;
import ru.santaev.clipboardtranslator.util.AppPreference;
import ru.santaev.clipboardtranslator.util.NotificationHelper;
import ru.santaev.clipboardtranslator.util.RxHelper;
import rx.Observable;
import rx.Single;

public class TranslateService extends Service implements ClipboardManager.OnPrimaryClipChangedListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "TranslateService";
    private static final int SERVICE_NOTIFICATION_ID = -1;
    private ClipboardManager clipboardManager;
    private IClipboardFilter filter;
    private ITranslationSettingsProvider translationSettingsProvider;

    public TranslateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");

        filter = new ClipboardFilter();
        translationSettingsProvider =  AppPreference.getInstance();
        AppPreference.getAppSharedPreference().registerOnSharedPreferenceChangeListener(this);

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
    }

    private void initClipboardListener(){
        clipboardManager = (ClipboardManager) getSystemService(Application.CLIPBOARD_SERVICE);
        clipboardManager.addPrimaryClipChangedListener(this);
    }

    private void releaseClipboardListener(){
        clipboardManager.removePrimaryClipChangedListener(this);
        clipboardManager = null;
    }

    private void showAppNotification(){
        String originLang = translationSettingsProvider.getOriginLang().getCode();
        String targetLang = translationSettingsProvider.getTargetLang().getCode();

        String langText = String.format("Translate from %s to %s", originLang, targetLang);

        Notification notification = NotificationHelper.buildAppNotification(getApplicationContext(), langText);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Application.NOTIFICATION_SERVICE);
        notificationManager.notify(SERVICE_NOTIFICATION_ID, notification);
    }

    private void hideAppNotification(){
        NotificationManager notificationManager = (NotificationManager) getSystemService(Application.NOTIFICATION_SERVICE);
        notificationManager.cancel(SERVICE_NOTIFICATION_ID);
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
        String originLang = translationSettingsProvider.getOriginLang().getCode();
        String targetLang = translationSettingsProvider.getTargetLang().getCode();

        int id = new Random().nextInt();
        String langText = String.format("%s-%s", originLang, targetLang);
        showNotification(String.format("Translating \"%s\"", text), langText, id);

        RxHelper.make(() -> {
            TranslateRequest request = new TranslateRequest(text, originLang,
                    targetLang);
            TranslateResponse response = ApiService.getInstance().translate(request);
            ArrayList<String> strings = response.getText();
            String translatedText = strings.size() > 0 ? strings.get(0) : "";
            showNotification(translatedText, langText, id);

            Translation translation = new Translation(originLang, targetLang, text, translatedText);
            AppDatabase.getInstance().getTranslationDao().insert(translation);
            return response;
        }, throwable -> showNotification("Translate failed", langText, id), null, null);
    }

    private void showNotification(String text, String langText, int id){
        Notification notification = NotificationHelper.buildTranslateNotification(getApplicationContext(), text, langText);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Application.NOTIFICATION_SERVICE);
        notificationManager.notify(id, notification);

        new Handler(Looper.getMainLooper()).post(() ->
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (AppPreference.KEY_ORIGIN_LANG.equals(key)
                || AppPreference.KEY_TARGET_LANG.equals(key)){
            showAppNotification();
        }
    }
}
