package ru.santaev.clipboardtranslator.ui;

import android.arch.lifecycle.LifecycleActivity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import ru.santaev.clipboardtranslator.R;
import ru.santaev.clipboardtranslator.databinding.ActivitySettingsBinding;
import ru.santaev.clipboardtranslator.ui.util.SettingsItem;
import ru.santaev.clipboardtranslator.util.AppPreference;

public class SettingsActivity extends LifecycleActivity {


    private AppPreference appPreference;
    private ActivitySettingsBinding binding;

    private SettingsItem settingsItemNotif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appPreference = AppPreference.getInstance();

        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);

        setActionBar(binding.toolbar);

        getActionBar().setTitle(R.string.settings_activity_title);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        initSettings();
    }

    private void initSettings() {
        settingsItemNotif = new SettingsItem("Уведомления при переводе", "Нотификация");

        binding.itemNotif.setItem(settingsItemNotif);
    }

}
