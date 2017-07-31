package ru.santaev.clipboardtranslator.service.uitl;


import ru.santaev.clipboardtranslator.model.Language;

public interface ITranslationSettingsProvider {

    Language getOriginLang();

    Language getTargetLang();
}
