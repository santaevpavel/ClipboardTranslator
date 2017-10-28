package ru.santaev.clipboardtranslator.util;


import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.res.ResourcesCompat;

import ru.santaev.clipboardtranslator.R;
import ru.santaev.clipboardtranslator.ui.MainActivity;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

public class NotificationHelper {

    public static final String KEY_TEXT = "KEY_TEXT";
    private static final String TRANSLATE = "Translate";

    public static Notification buildAppNotification(Context context, String langText){
        PendingIntent intent = PendingIntent.getActivity(context, 0,
                new Intent(context.getApplicationContext(), MainActivity.class), FLAG_UPDATE_CURRENT);
        return new NotificationCompat.Builder(context, TRANSLATE)
                .setAutoCancel(false)
                .setColor(ResourcesCompat.getColor(context.getResources(), R.color.colorPrimary, null))
                .setOngoing(true)
                .setSmallIcon(R.drawable.icon_status_bar)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(context.getString(R.string.app_notification_title))
                .setContentText(context.getString(R.string.app_notification_content_text))
                .setSubText(langText)
                .setContentIntent(intent)
                .build();
    }

    public static Notification buildTranslateNotification(Context context, String message, String langText,
                                                          int requestCodeCopy, String action) {
        Intent intent = new Intent(action);
        intent.putExtra(KEY_TEXT, message);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCodeCopy, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        return getTranslateNotificationBuilder(context, message, langText)
                .addAction(R.drawable.ic_content_copy_24dp, context.getString(R.string.notification_button_copy), pendingIntent)
                .build();
    }

    public static Notification buildTranslateNotification(Context context, String message, String langText){
        return getTranslateNotificationBuilder(context, message, langText).build();
    }

    private static NotificationCompat.Builder getTranslateNotificationBuilder(Context context, String message, String langText) {
        return new NotificationCompat.Builder(context, TRANSLATE)
                .setAutoCancel(false)
                .setColor(ResourcesCompat.getColor(context.getResources(), R.color.colorPrimary, null))
                .setOngoing(false)
                .setSmallIcon(R.drawable.icon_status_bar)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(context.getString(R.string.translate_notification_title))
                .setContentInfo(langText)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message));
    }

}
