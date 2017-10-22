package ru.santaev.clipboardtranslator.service.uitl;


import android.content.ClipData;
import android.content.ClipDescription;

public class ClipboardFilter implements IClipboardFilter{

    @Override
    public boolean filter(ClipData.Item item, ClipDescription clipDescription) {
        /*String mimeType = clipDescription.getMimeType(0);
        CharSequence text = item.getText();
        return mimeType.equals(ClipDescription.MIMETYPE_TEXT_PLAIN);*/
        return true;
    }
}
