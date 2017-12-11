package ru.santaev.clipboardtranslator.service.util;


import android.content.ClipData;
import android.content.ClipDescription;

public interface IClipboardFilter {

    boolean apply(ClipData.Item item, ClipDescription clipDescription);
}
