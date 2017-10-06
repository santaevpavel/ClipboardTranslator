package ru.santaev.clipboardtranslator.ui;

import android.arch.lifecycle.LifecycleActivity;
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
import ru.santaev.clipboardtranslator.model.IDataModel;
import ru.santaev.clipboardtranslator.ui.adapter.LanguageAdapter;

public class ChooseLanguageActivity extends LifecycleActivity {

    public static final String ARG_KEY_LANG_ORIGIN = "ARG_KEY_LANG_ORIGIN";
    public static final String ARG_KEY_LANG_TARGET = "ARG_KEY_LANG_TARGET";
    public static final String ARG_KEY_CHOOSE_ORIGIN = "ARG_KEY_CHOOSE_ORIGIN";

    public static final String RESULT_KEY_LANG = "RESULT_KEY_LANG";
    private List<Language> languages;

    public static Intent getIntent(Context context, Language origin, Language target,
                                   boolean chooseOrigin){
        Intent intent = new Intent(context, ChooseLanguageActivity.class);
        intent.putExtra(ARG_KEY_LANG_ORIGIN, origin);
        intent.putExtra(ARG_KEY_LANG_TARGET, target);
        intent.putExtra(ARG_KEY_CHOOSE_ORIGIN, chooseOrigin);
        return intent;
    }

    private IDataModel dataModel;

    private ActivityChooseLanguageBinding binding;
    private LanguageAdapter adapter;

    private Language langOrigin;
    private Language langTarget;
    private boolean chooseOrigin;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        extractArguments();

        dataModel = TranslatorApp.getInstance().getDataModel();

        binding = DataBindingUtil.setContentView(this, R.layout.activity_choose_language);

        setActionBar(binding.toolbar);

        getActionBar().setTitle(R.string.choose_lang_activity_title);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        dataModel.getLanguages().observe(this, languages -> {
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

        adapter = new LanguageAdapter(supportedList, unsupportedList);

        binding.list.setLayoutManager(new LinearLayoutManager(this));
        binding.list.setAdapter(adapter);

        adapter.setListener(translation -> {
            Intent data = new Intent();
            data.putExtra(RESULT_KEY_LANG, translation);
            setResult(RESULT_OK, data);
            finish();
        });
    }
}
