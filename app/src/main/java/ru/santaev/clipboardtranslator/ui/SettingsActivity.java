package ru.santaev.clipboardtranslator.ui;

import android.arch.lifecycle.LifecycleActivity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import ru.santaev.clipboardtranslator.R;
import ru.santaev.clipboardtranslator.databinding.ActivitySettingsBinding;
import ru.santaev.clipboardtranslator.ui.settings.ListSettingsItem;
import ru.santaev.clipboardtranslator.ui.settings.SettingsItem;
import ru.santaev.clipboardtranslator.util.settings.AppPreference;
import ru.santaev.clipboardtranslator.util.settings.ISettings;

public class SettingsActivity extends LifecycleActivity {


    private ISettings settings;
    private ActivitySettingsBinding binding;

    private ListSettingsItem settingsItemNotif;
    private SettingsItem settingsItemFeedback;
    private SettingsItem settingsItemRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        settingsItemNotif = new ListSettingsItem(this, R.string.settings_item_notif_title,
                R.array.settings_item_notif_text, R.array.settings_item_notif_value,
                settings::setNotificationType, settings::getNotificationType);
        binding.itemNotif.setItem(settingsItemNotif);
        binding.itemNotif.getRoot().setOnClickListener((v) -> settingsItemNotif.onClick());

        settingsItemFeedback = new SettingsItem(getString(R.string.settings_item_feedback_title),
                getString(R.string.settings_item_feedback_subtitle));
        binding.itemFeedback.setItem(settingsItemFeedback);
        binding.itemFeedback.getRoot().setOnClickListener((v) -> email());

        settingsItemRate = new SettingsItem(getString(R.string.settings_item_rate_title),
                getString(R.string.settings_item_rate_subtitle));
        binding.itemRate.setItem(settingsItemRate);
        binding.itemRate.getRoot().setOnClickListener((v) -> launchGooglePLay());

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
