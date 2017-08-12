package ru.santaev.clipboardtranslator.ui.adapter;


import android.support.annotation.DrawableRes;

import ru.santaev.clipboardtranslator.R;
import ru.santaev.clipboardtranslator.model.Language;

public class LanguageIconProvider {

    @DrawableRes
    public int getLanguageIcon(Language language) {
        switch (language) {
            case RUSSIAN:
                return R.drawable.ic_country_russia;
            case BELORUSSIAN:
                return R.drawable.ic_country_belorussia;
            case UKRAINIAN:
                return R.drawable.ic_country_ukraine;
            case ENGLISH:
                return R.drawable.ic_country_usa;
            case FRENCH:
                return R.drawable.ic_country_france;
            case GERMAN:
                return R.drawable.ic_country_german;
            case SPAIN:
                return R.drawable.ic_country_spain;
            case ITALIAN:
                return R.drawable.ic_country_italy;
            case HOLLAND:
                return R.drawable.ic_country_holland;
            case PORTUGAL:
                return R.drawable.ic_country_portugal;
            default:
                return R.drawable.ic_country_unknown;
        }
    }
}
