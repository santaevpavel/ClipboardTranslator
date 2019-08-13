package ru.santaev.clipboardtranslator.util;

import androidx.databinding.BindingAdapter;
import android.view.View;
import android.view.ViewGroup;

public class BindingUtil {

    @BindingAdapter("layout_height")
    public static void setLayoutHeight(View view, float height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();

        if (height < 0) {
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        } else {
            layoutParams.height = (int) height;
        }
        view.setLayoutParams(layoutParams);
    }
}
