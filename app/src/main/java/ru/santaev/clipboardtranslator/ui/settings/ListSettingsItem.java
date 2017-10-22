package ru.santaev.clipboardtranslator.ui.settings;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;

public class ListSettingsItem extends SettingsItem {

    public interface ISettingsPropertySetter {
        void setValue(int value);
    }

    public interface ISettingsPropertyGetter {
        int getValue();
    }

    private final int textArray;
    private final int[] valueArray;
    private final String titleString;
    private String[] textArrayRes;
    private final Context context;
    private final ISettingsPropertySetter setter;
    private final ISettingsPropertyGetter getter;

    public ListSettingsItem(Context context, int title, int arrayText, int arrayValue,
                            ISettingsPropertySetter setter, ISettingsPropertyGetter getter) {
        super("", "");
        this.context = context;
        this.setter = setter;
        this.getter = getter;

        Resources resources = context.getResources();
        textArray = arrayText;
        valueArray = resources.getIntArray(arrayValue);
        textArrayRes = context.getResources().getStringArray(textArray);
        titleString = resources.getString(title);

        this.title.set(titleString);
        update();
    }

    public void onClick() {
        new AlertDialog.Builder(context)
                .setTitle(titleString)
                .setItems(textArray, (dialogInterface, i) -> {
                    this.subtitle.set(textArrayRes[i]);
                    setter.setValue(valueArray[i]);
                    dialogInterface.dismiss();
                })
                .show();
    }

    private void update() {
        String text = textArrayRes[getIdx(getter.getValue())];
        this.subtitle.set(text);
    }

    private int getIdx(int value) {
        for (int i = 0; i < valueArray.length; i++) {
            if (valueArray[i] == value) {
                return i;
            }
        }
        return 0;
    }
}
