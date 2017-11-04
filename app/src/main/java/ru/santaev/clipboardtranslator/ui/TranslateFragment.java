package ru.santaev.clipboardtranslator.ui;


import android.animation.LayoutTransition;
import android.app.Activity;
import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import ru.santaev.clipboardtranslator.R;
import ru.santaev.clipboardtranslator.TranslatorApp;
import ru.santaev.clipboardtranslator.databinding.FragmentTranslateBinding;
import ru.santaev.clipboardtranslator.db.entity.Language;
import ru.santaev.clipboardtranslator.model.TranslateDirectionProvider;
import ru.santaev.clipboardtranslator.service.TranslateService;
import ru.santaev.clipboardtranslator.util.Analytics;
import ru.santaev.clipboardtranslator.viewmodel.TranslateViewModel;

import static ru.santaev.clipboardtranslator.util.Analytics.EVENT_ID_NAME_CLICK_CLEAR_TEXT;
import static ru.santaev.clipboardtranslator.util.Analytics.EVENT_ID_NAME_CLICK_OPEN_YANDEX;
import static ru.santaev.clipboardtranslator.util.Analytics.EVENT_ID_NAME_CLICK_RETRY;
import static ru.santaev.clipboardtranslator.util.Analytics.EVENT_ID_NAME_CLICK_SOURCE_LANG;
import static ru.santaev.clipboardtranslator.util.Analytics.EVENT_ID_NAME_CLICK_START_SERVICE;
import static ru.santaev.clipboardtranslator.util.Analytics.EVENT_ID_NAME_CLICK_STOP_SERVICE;
import static ru.santaev.clipboardtranslator.util.Analytics.EVENT_ID_NAME_CLICK_TARGET_LANG;
import static ru.santaev.clipboardtranslator.util.Analytics.EVENT_ID_NAME_TRANSLATED;
import static ru.santaev.clipboardtranslator.util.Analytics.EVENT_ID_NAME_TRANSLATE_FAILED;
import static ru.santaev.clipboardtranslator.util.Analytics.EVENT_ID_SELECT_SOURCE_LANG;
import static ru.santaev.clipboardtranslator.util.Analytics.EVENT_ID_SELECT_TARGET_LANG;

public class TranslateFragment extends LifecycleFragment {

    private static final int REQUEST_CODE_ORIGIN_LANG = 0;
    private static final int REQUEST_CODE_TARGET_LANG = 1;

    private TranslateViewModel viewModel;
    private FragmentTranslateBinding binding;
    private TranslateDirectionProvider translateDirectionProvider;

    private Analytics analytics;

    public TranslateFragment() {
        // Required empty public constructor
    }

    public static TranslateFragment newInstance() {
        TranslateFragment fragment = new TranslateFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        analytics = new Analytics(getActivity());
        translateDirectionProvider = new TranslateDirectionProvider();
        ViewModelFactory factory = new ViewModelFactory(TranslatorApp.getInstance().getDataModel());
        viewModel = ViewModelProviders.of(this, factory).get(TranslateViewModel.class);
        observeModel();

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_translate, container, false);

        binding.originTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                viewModel.onOriginTextChanged(s.toString());
                binding.clear.setVisibility(s.length() == 0 ? View.INVISIBLE : View.VISIBLE);
            }
        });

        binding.clear.setOnClickListener(v -> {
            analytics.logClickEvent(EVENT_ID_NAME_CLICK_CLEAR_TEXT);
            binding.originTextView.setText("");
        });

        binding.originLangText.setOnClickListener(v -> chooseOriginLang());

        binding.targetLangText.setOnClickListener(v -> chooseTargetLang());

        binding.startService.setOnClickListener(v -> {
            analytics.logClickEvent(EVENT_ID_NAME_CLICK_START_SERVICE);
            getActivity().startService(new Intent(getContext(), TranslateService.class));
        });

        binding.stopService.setOnClickListener(v -> {
            analytics.logClickEvent(EVENT_ID_NAME_CLICK_STOP_SERVICE);
            getActivity().stopService(new Intent(getContext(), TranslateService.class));
        });

        binding.retry.setOnClickListener(v -> {
            analytics.logClickEvent(EVENT_ID_NAME_CLICK_RETRY);
            viewModel.onClickRetry();
        });

        binding.translatedByYandex.setOnClickListener(this::openYandexTranslate);

        enableAnimation();
        return binding.getRoot();
    }

    private void chooseOriginLang(){
        analytics.logClickEvent(EVENT_ID_NAME_CLICK_SOURCE_LANG);

        Intent intent = ChooseLanguageActivity.getIntent(getContext(),
                viewModel.getOriginLang().getValue(),
                viewModel.getTargetLang().getValue(), true);

        startActivityForResult(intent, REQUEST_CODE_ORIGIN_LANG);
    }

    private void chooseTargetLang(){
        analytics.logClickEvent(EVENT_ID_NAME_CLICK_TARGET_LANG);

        Intent intent = ChooseLanguageActivity.getIntent(getContext(),
                viewModel.getOriginLang().getValue(),
                viewModel.getTargetLang().getValue(), false);
        startActivityForResult(intent, REQUEST_CODE_TARGET_LANG);
    }

    private void observeModel(){
        viewModel.getTranslatedText().observe(this, s -> {
            if (null != binding) {
                if (s == null || s.isEmpty()){
                    binding.translateLayout.setVisibility(View.GONE);
                } else {
                    analytics.logClickEvent(EVENT_ID_NAME_TRANSLATED);
                    binding.translateLayout.setVisibility(View.VISIBLE);
                    binding.translatedTextView.setText(s);
                }
            }
        });

        viewModel.getProgress().observe(this, progress -> {
            if (null != binding) {
                if (progress != null && progress) {
                    binding.translateLayout.setVisibility(View.VISIBLE);
                }
                binding.translateProgress.setVisibility(progress ? View.VISIBLE : View.INVISIBLE);
            }
        });

        viewModel.getOriginLang().observe(this, language -> {
            if (null != binding && language != null) {
                binding.originLangText.setText(language.getName());
            }
        });

        viewModel.getTargetLang().observe(this, language -> {
            if (null != binding && language != null) {
                binding.targetLangText.setText(language.getName());
            }
        });

        viewModel.getFailed().observe(this, isFailed -> {
            if (null != binding && isFailed != null) {
                if (isFailed) {
                    analytics.logClickEvent(EVENT_ID_NAME_TRANSLATE_FAILED);
                }
                binding.retry.setVisibility(isFailed ? View.VISIBLE : View.GONE);
                binding.translatedByYandex.setVisibility(isFailed ? View.GONE : View.VISIBLE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK){
            return;
        }
        switch (requestCode){
            case REQUEST_CODE_ORIGIN_LANG:
                Language lang = (Language) data.getSerializableExtra(ChooseLanguageActivity.RESULT_KEY_LANG);
                analytics.logSelectEvent(EVENT_ID_SELECT_SOURCE_LANG + lang.getCode(), lang.getName());
                viewModel.onOriginLangSelected(lang);
                break;
            case REQUEST_CODE_TARGET_LANG:
                lang = (Language) data.getSerializableExtra(ChooseLanguageActivity.RESULT_KEY_LANG);
                analytics.logSelectEvent(EVENT_ID_SELECT_TARGET_LANG + lang.getCode(), lang.getName());
                viewModel.onTargetLangSelected(lang);
                break;
        }
    }

    private void openYandexTranslate(View view) {
        analytics.logClickEvent(EVENT_ID_NAME_CLICK_OPEN_YANDEX);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.yandex_translate_url)));
        startActivity(browserIntent);
    }

    private void enableAnimation() {
        binding.rootLinear.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
    }
}
