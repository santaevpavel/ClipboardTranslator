package ru.santaev.clipboardtranslator.ui;

import android.arch.lifecycle.LifecycleActivity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Locale;

import ru.santaev.clipboardtranslator.R;
import ru.santaev.clipboardtranslator.databinding.ActivitySettingsBinding;
import ru.santaev.clipboardtranslator.ui.settings.ListSettingsItem;
import ru.santaev.clipboardtranslator.ui.settings.SettingsItem;
import ru.santaev.clipboardtranslator.util.Analytics;
import ru.santaev.clipboardtranslator.util.settings.AppPreference;
import ru.santaev.clipboardtranslator.util.settings.ISettings;

import static ru.santaev.clipboardtranslator.util.Analytics.EVENT_ID_NAME_CLICK_SETTINGS_COPY_BUTTON;
import static ru.santaev.clipboardtranslator.util.Analytics.EVENT_ID_NAME_CLICK_SETTINGS_FEEDBACK;
import static ru.santaev.clipboardtranslator.util.Analytics.EVENT_ID_NAME_CLICK_SETTINGS_NOTIFICATION_TYPE;
import static ru.santaev.clipboardtranslator.util.Analytics.EVENT_ID_NAME_CLICK_SETTINGS_RATE;

public class SettingsActivity extends LifecycleActivity {

    private ISettings settings;
    private ActivitySettingsBinding binding;

    private ListSettingsItem settingsItemNotificationType;
    private ListSettingsItem settingsItemNotificationCopyButton;
    private SettingsItem settingsItemFeedback;
    private SettingsItem settingsItemRate;
    private SettingsItem settingsItemBuildVersion;

    private Analytics analytics;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        analytics = new Analytics(this);
        settings = AppPreference.getInstance();

        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);

        setActionBar(binding.toolbar);

        getActionBar().setTitle(R.string.settings_activity_title);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        initSettings();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initSettings() {
        // Notification type
        settingsItemNotificationType = new ListSettingsItem(this, R.string.settings_item_notif_title,
                R.array.settings_item_notification_type_text, R.array.settings_item_notification_type_value,
                settings::setNotificationType, settings::getNotificationType);
        binding.itemNotif.setItem(settingsItemNotificationType);
        binding.itemNotif.getRoot().setOnClickListener((v) -> {
            analytics.logClickEvent(EVENT_ID_NAME_CLICK_SETTINGS_NOTIFICATION_TYPE);
            settingsItemNotificationType.onClick();
        });

        // Copy button
        settingsItemNotificationCopyButton = new ListSettingsItem(this, R.string.settings_item_notif_btn_title,
                R.array.settings_item_notification_btn_text, R.array.settings_item_notification_btn_value,
                value -> settings.setNotificationButtonEnabled(value != 0), () -> settings.isNotificationButtonEnabled() ? 1 : 0);
        binding.itemNotifCopyButton.setItem(settingsItemNotificationCopyButton);
        binding.itemNotifCopyButton.getRoot().setOnClickListener((v) -> {
            analytics.logClickEvent(EVENT_ID_NAME_CLICK_SETTINGS_COPY_BUTTON);
            settingsItemNotificationCopyButton.onClick();
        });

        // Feedback
        settingsItemFeedback = new SettingsItem(getString(R.string.settings_item_feedback_title),
                getString(R.string.settings_item_feedback_subtitle));
        binding.itemFeedback.setItem(settingsItemFeedback);
        binding.itemFeedback.getRoot().setOnClickListener((v) -> {
            analytics.logClickEvent(EVENT_ID_NAME_CLICK_SETTINGS_FEEDBACK);
            email();
        });

        // Rate
        settingsItemRate = new SettingsItem(getString(R.string.settings_item_rate_title),
                getString(R.string.settings_item_rate_subtitle));
        binding.itemRate.setItem(settingsItemRate);
        binding.itemRate.getRoot().setOnClickListener((v) -> {
            analytics.logClickEvent(EVENT_ID_NAME_CLICK_SETTINGS_RATE);
            launchGooglePLay();
        });

        // App version
        settingsItemBuildVersion = new SettingsItem(getString(R.string.settings_item_build_title),
                getBuildVersion());
        binding.itemBuildVersion.setItem(settingsItemBuildVersion);
    }

    private String getBuildVersion() {
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String versionName = pInfo.versionName;
            return String.format(Locale.ENGLISH, "%s (%d)", versionName, pInfo.versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "-";
    }

    private void launchGooglePLay() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }

    private void email() {
        String mailTo = "mailto:santaevp@gmail.com";

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse(mailTo));

        try {
            startActivity(emailIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Unable to find mail app!", Toast.LENGTH_LONG).show();
        }
    }

}
