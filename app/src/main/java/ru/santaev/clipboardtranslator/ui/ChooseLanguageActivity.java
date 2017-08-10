package ru.santaev.clipboardtranslator.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;

import java.util.Arrays;

import ru.santaev.clipboardtranslator.R;
import ru.santaev.clipboardtranslator.databinding.ActivityChooseLanguageBinding;
import ru.santaev.clipboardtranslator.model.Language;
import ru.santaev.clipboardtranslator.ui.adapter.LanguageAdapter;

public class ChooseLanguageActivity extends AppCompatActivity {

    public static final String RESULT_KEY_LANG = "RESULT_KEY_LANG";

    private ActivityChooseLanguageBinding binding;
    private LanguageAdapter adapter;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_choose_language);

        setSupportActionBar(binding.toolbar);

        getSupportActionBar().setTitle("Выберите язык");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter = new LanguageAdapter(Arrays.asList(Language.values()), Arrays.asList(Language.values()));

        binding.list.setLayoutManager(new LinearLayoutManager(this));
        binding.list.setAdapter(adapter);

        adapter.setListener(translation -> {
            Intent data = new Intent();
            data.putExtra(RESULT_KEY_LANG, translation);
            setResult(RESULT_OK, data);
            finish();
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
}
