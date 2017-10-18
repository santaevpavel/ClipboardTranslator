package ru.santaev.clipboardtranslator.ui.util;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;

public class SettingsItem extends BaseObservable {

    @Bindable
    public final ObservableField<String> title = new ObservableField<>();

    @Bindable
    public final ObservableField<String> subtitle = new ObservableField<>();

    public SettingsItem(String title, String subtitle) {
        this.title.set(title);
        this.subtitle.set(subtitle);
    }
}
