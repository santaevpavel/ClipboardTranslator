package ru.santaev.clipboardtranslator.service.uitl;


import android.content.ClipData;
import android.content.ClipDescription;

import static ru.santaev.clipboardtranslator.service.TranslateService.CLIPBOARD_DATA_LABEL;

public class ClipboardFilter implements IClipboardFilter{

    @Override
    public boolean filter(ClipData.Item item, ClipDescription clipDescription) {
        return !CLIPBOARD_DATA_LABEL.equals(clipDescription.getLabel());
    }
}
