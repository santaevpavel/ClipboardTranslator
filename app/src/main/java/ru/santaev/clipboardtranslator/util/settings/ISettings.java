package ru.santaev.clipboardtranslator.util.settings;

public interface ISettings {

    int NOTIFICATION_DELAY_NONE = 1;
    int NOTIFICATION_DELAY_FIVE = 2;
    int NOTIFICATION_DELAY_TEN = 3;
    int NOTIFICATION_DELAY_TWENTY = 4;

    int NOTIFICATION_TYPE_PUSH = 1;
    int NOTIFICATION_TYPE_TOAST = 2;

    int getNotificationType();

    void setNotificationType(int type);

    boolean isNotificationButtonEnabled();

    void setNotificationButtonEnabled(boolean enabled);

    int getNotificationClearDelay();

    void setNotificationClearDelay(int delay);
}
