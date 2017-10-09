package ru.santaev.clipboardtranslator.ui;


import android.app.Activity;
import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.santaev.clipboardtranslator.R;
import ru.santaev.clipboardtranslator.TranslatorApp;
import ru.santaev.clipboardtranslator.databinding.FragmentTranslateBinding;
import ru.santaev.clipboardtranslator.db.entity.Language;
import ru.santaev.clipboardtranslator.model.TranslateDirectionProvider;
import ru.santaev.clipboardtranslator.service.TranslateService;
import ru.santaev.clipboardtranslator.viewmodel.TranslateViewModel;

public class TranslateFragment extends LifecycleFragment {

    private static final int REQUEST_CODE_ORIGIN_LANG = 0;
    private static final int REQUEST_CODE_TARGET_LANG = 1;

    private TranslateViewModel viewModel;
    private FragmentTranslateBinding binding;
    private TranslateDirectionProvider translateDirectionProvider;

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
        translateDirectionProvider = new TranslateDirectionProvider();
        ViewModelFactory factory = new ViewModelFactory(TranslatorApp.getInstance().getDataModel());
        viewModel = ViewModelProviders.of(this, factory).get(TranslateViewModel.class);
        observeModel();
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

        binding.clear.setOnClickListener(v -> binding.originTextView.setText(""));

        binding.originLangText.setOnClickListener(v -> chooseOriginLang());

        binding.targetLangText.setOnClickListener(v -> chooseTargetLang());

        binding.startService.setOnClickListener(v -> getActivity().startService(new Intent(getContext(), TranslateService.class)));

        binding.stopService.setOnClickListener(v -> getActivity().stopService(new Intent(getContext(), TranslateService.class)));

        binding.retry.setOnClickListener(v -> viewModel.onClickRetry());

        return binding.getRoot();
    }

    private void chooseOriginLang(){
        Intent intent = ChooseLanguageActivity.getIntent(getContext(),
                viewModel.getOriginLang().getValue(),
                viewModel.getTargetLang().getValue(), true);
        startActivityForResult(intent, REQUEST_CODE_ORIGIN_LANG);
    }

    private void chooseTargetLang(){
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
                    binding.translateLayout.setVisibility(View.VISIBLE);
                    binding.translatedTextView.setText(s);
                }
            }
        });

        viewModel.getProgress().observe(this, progress -> {
            if (null != binding) {
                if (progress) {
                    binding.translateLayout.setVisibility(View.VISIBLE);
                }
                binding.translateProgress.setVisibility(progress ? View.VISIBLE : View.INVISIBLE);
            }
        });

        viewModel.getOriginLang().observe(this, language -> {
            if (null != binding) {
                binding.originLangText.setText(language.getName());
            }
        });

        viewModel.getTargetLang().observe(this, language -> {
            if (null != binding) {
                binding.targetLangText.setText(language.getName());
            }
        });

        viewModel.getFailed().observe(this, isFailed -> {
            if (null != binding) {
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
                viewModel.onOriginLangSelected(lang);
                break;
            case REQUEST_CODE_TARGET_LANG:
                lang = (Language) data.getSerializableExtra(ChooseLanguageActivity.RESULT_KEY_LANG);
                viewModel.onTargetLangSelected(lang);
                break;
        }
    }

}
