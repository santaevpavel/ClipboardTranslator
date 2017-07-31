package ru.santaev.clipboardtranslator.service.uitl;


import android.content.ClipData;
import android.content.ClipDescription;

public interface IClipboardFilter {

    boolean filter(ClipData.Item item, ClipDescription clipDescription);
}
