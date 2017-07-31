package ru.santaev.clipboardtranslator.util;


import android.app.Notification;
import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.NotificationCompat;

import ru.santaev.clipboardtranslator.R;

public class NotificationHelper {

    public static Notification buildAppNotification(Context context, String langText){
        return new NotificationCompat.Builder(context)
                .setAutoCancel(true)
                .setColor(ResourcesCompat.getColor(context.getResources(), R.color.colorAccent, null))
                .setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .setContentTitle("Clipboard translator")
                .setContentText("Clipboard translator is running...")
                //.setContentInfo(langText)
                .setSubText(langText)
                .build();
    }

    public static Notification buildTranslateNotification(Context context, String message, String langText){
        return new NotificationCompat.Builder(context)
                .setAutoCancel(true)
                .setColor(ResourcesCompat.getColor(context.getResources(), R.color.colorAccent, null))
                .setOngoing(false)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .setContentTitle("Translate from clipboard")
                .setContentInfo(langText)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message))
                .build();
    }

}
