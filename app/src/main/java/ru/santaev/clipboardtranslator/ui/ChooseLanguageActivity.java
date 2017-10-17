package ru.santaev.clipboardtranslator.ui;

import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import ru.santaev.clipboardtranslator.R;
import ru.santaev.clipboardtranslator.TranslatorApp;
import ru.santaev.clipboardtranslator.databinding.ActivityChooseLanguageBinding;
import ru.santaev.clipboardtranslator.db.entity.Language;
import ru.santaev.clipboardtranslator.ui.adapter.LanguageAdapter;
import ru.santaev.clipboardtranslator.util.AppPreference;
import ru.santaev.clipboardtranslator.viewmodel.ChooseLanguageViewModel;

public class ChooseLanguageActivity extends LifecycleActivity {

    public static final String ARG_KEY_LANG_ORIGIN = "ARG_KEY_LANG_ORIGIN";
    public static final String ARG_KEY_LANG_TARGET = "ARG_KEY_LANG_TARGET";
    public static final String ARG_KEY_CHOOSE_ORIGIN = "ARG_KEY_CHOOSE_ORIGIN";
    public static final String RESULT_KEY_LANG = "RESULT_KEY_LANG";

    public static final int RECENT_LANGUAGES_MAX_SIZE = 3;

    private List<Language> languages;
    private ChooseLanguageViewModel viewModel;

    public static Intent getIntent(Context context, Language origin, Language target,
                                   boolean chooseOrigin){
        Intent intent = new Intent(context, ChooseLanguageActivity.class);
        intent.putExtra(ARG_KEY_LANG_ORIGIN, origin);
        intent.putExtra(ARG_KEY_LANG_TARGET, target);
        intent.putExtra(ARG_KEY_CHOOSE_ORIGIN, chooseOrigin);
        return intent;
    }

    private ActivityChooseLanguageBinding binding;
    private LanguageAdapter adapter;

    private Language langOrigin;
    private Language langTarget;
    private boolean chooseOrigin;
    private AppPreference appPreference;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        extractArguments();

        viewModel = ViewModelProviders.of(this, new ViewModelFactory(TranslatorApp.getInstance().getDataModel()))
                .get(ChooseLanguageViewModel.class);
        appPreference = AppPreference.getInstance();

        binding = DataBindingUtil.setContentView(this, R.layout.activity_choose_language);

        setActionBar(binding.toolbar);

        getActionBar().setTitle(R.string.choose_lang_activity_title);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        viewModel.getLanguages().observe(this, languages -> {
            this.languages = languages;
            if (languages != null) {
                initAdapter();
            }
        });

        setResult(RESULT_CANCELED);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void extractArguments(){
        langOrigin = (Language) getIntent().getSerializableExtra(ARG_KEY_LANG_ORIGIN);
        langTarget = (Language) getIntent().getSerializableExtra(ARG_KEY_LANG_TARGET);
        chooseOrigin = getIntent().getBooleanExtra(ARG_KEY_CHOOSE_ORIGIN, true);
    }

    private void initAdapter(){
        List<Language> supportedList;
        List<Language> unsupportedList;

        /*TranslateDirectionProvider translateDirectionProvider = new TranslateDirectionProvider();

        if (chooseOrigin){
            supportedList = translateDirectionProvider.getSupportedOriginLanguages(langTarget);
        } else {
            supportedList = translateDirectionProvider.getSupportedTargetLanguages(langOrigin);
        }*/
        supportedList = languages;

        unsupportedList = new ArrayList<>();
        unsupportedList.removeAll(supportedList);

        List<Language> recentLanguages = appPreference.getLastUsedLanguages();

        adapter = new LanguageAdapter(recentLanguages, supportedList, unsupportedList);

        binding.list.setLayoutManager(new LinearLayoutManager(this));
        binding.list.setAdapter(adapter);

        adapter.setListener(translation -> {
            addLangToRecent(translation);
            Intent data = new Intent();
            data.putExtra(RESULT_KEY_LANG, translation);
            setResult(RESULT_OK, data);
            finish();
        });
    }

    private void addLangToRecent(Language language) {
        List<Language> recentLanguages = appPreference.getLastUsedLanguages();
        if (!recentLanguages.contains(language)) {
            if (RECENT_LANGUAGES_MAX_SIZE <= recentLanguages.size()) {
                recentLanguages.remove(recentLanguages.size() - 1);
            }
            recentLanguages.add(0, language);
            appPreference.setLastUsedLanguages(recentLanguages);
        }
    }
}
