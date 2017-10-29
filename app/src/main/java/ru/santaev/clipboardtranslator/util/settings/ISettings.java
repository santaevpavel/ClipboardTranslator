package ru.santaev.clipboardtranslator.util.settings;

public interface ISettings {

    int NOTIFICATION_TYPE_PUSH = 1;
    int NOTIFICATION_TYPE_TOAST = 2;

    int getNotificationType();

    void setNotificationType(int type);

    boolean isNotificationButtonEnabled();

    void setNotificationButtonEnabled(boolean enabled);
}
