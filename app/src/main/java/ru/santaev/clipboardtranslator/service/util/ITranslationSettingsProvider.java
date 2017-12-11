package ru.santaev.clipboardtranslator.service.util;


import ru.santaev.clipboardtranslator.db.entity.Language;

public interface ITranslationSettingsProvider {

    Language getOriginLang();

    Language getTargetLang();
}
