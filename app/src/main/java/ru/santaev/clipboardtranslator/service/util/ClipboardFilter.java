package ru.santaev.clipboardtranslator.service.util;


import android.content.ClipData;
import android.content.ClipDescription;

import ru.santaev.clipboardtranslator.service.TranslateService;

public class ClipboardFilter implements IClipboardFilter{

    @Override
    public boolean apply(ClipData.Item item, ClipDescription clipDescription) {
        return !TranslateService.CLIPBOARD_DATA_LABEL.equals(clipDescription.getLabel());
    }
}
